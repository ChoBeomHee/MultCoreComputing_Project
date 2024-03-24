#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>
#include <cuda_runtime.h>

#define CUDA 0
#define OPENMP 1
#define SPHERES 20
#define DIM 2048
#define rnd(x) (x * rand() / RAND_MAX)
#define INF 2e10f

struct Sphere {
    float r, b, g;
    float radius;
    float x, y, z;
    __device__ float hit(float ox, float oy, float* n) {
        float dx = ox - x;
        float dy = oy - y;
        if (dx * dx + dy * dy < radius * radius) {
            float dz = sqrtf(radius * radius - dx * dx - dy * dy);
            *n = dz / sqrtf(radius * radius);
            return dz + z;
        }
        return -INF;
    }
};

__global__ void kernel(Sphere* s, unsigned char* ptr) {
    int x = blockIdx.x * blockDim.x + threadIdx.x;
    int y = blockIdx.y * blockDim.y + threadIdx.y;
    int offset = x + y * DIM;
    float ox = (x - DIM / 2);
    float oy = (y - DIM / 2);

    float r = 0, g = 0, b = 0;
    float maxz = -INF;
    for (int i = 0; i < SPHERES; i++) {
        float n;
        float t = s[i].hit(ox, oy, &n);
        if (t > maxz) {
            float fscale = n;
            r = s[i].r * fscale;
            g = s[i].g * fscale;
            b = s[i].b * fscale;
            maxz = t;
        }
    }

    // 이미지 픽셀 값을 설정합니다.
    ptr[offset * 4 + 0] = (int)(r * 255);
    ptr[offset * 4 + 1] = (int)(g * 255);
    ptr[offset * 4 + 2] = (int)(b * 255);
    ptr[offset * 4 + 3] = 255;
}

int main(int argc, char* argv[]) {
    int no_threads;
    int option;
    unsigned char* bitmap;
    Sphere* temp_s = (Sphere*)malloc(sizeof(Sphere) * SPHERES);
    for (int i = 0; i < SPHERES; i++) {
        temp_s[i].r = rnd(1.0f);
        temp_s[i].g = rnd(1.0f);
        temp_s[i].b = rnd(1.0f);
        temp_s[i].x = rnd(2000.0f) - 1000;
        temp_s[i].y = rnd(2000.0f) - 1000;
        temp_s[i].z = rnd(2000.0f) - 1000;
        temp_s[i].radius = rnd(200.0f) + 40;
    }
    cudaEvent_t start, stop;
    float elapsedTime;
    cudaEventCreate(&start);
    cudaEventCreate(&stop);

    if (argc != 3) {
        printf("> a.out [option] [filename.ppm]\n");
        printf("[option] 0: CUDA, 1~16: OpenMP using 1~16 threads\n");
        printf("for example, '> a.out 8 result.ppm' means executing OpenMP with 8 threads\n");
        exit(0);
    }
    FILE* fp = fopen(argv[2], "w");

    if (strcmp(argv[1], "0") == 0) {
        option = CUDA;
    } else {
        option = OPENMP;
        no_threads = atoi(argv[1]);
    }

    bitmap = (unsigned char*)malloc(sizeof(unsigned char) * DIM * DIM * 4);
    unsigned char* d_bitmap;
    Sphere* d_temp_s;
    cudaMalloc((void**)&d_bitmap, sizeof(unsigned char) * DIM * DIM * 4);
    cudaMalloc((void**)&d_temp_s, sizeof(Sphere) * SPHERES);

    cudaMemcpy(d_temp_s, temp_s, sizeof(Sphere) * SPHERES, cudaMemcpyHostToDevice);
    dim3 block(16, 16);
    dim3 grid(DIM / block.x, DIM / block.y);

    cudaEventRecord(start, 0);
    kernel<<<grid, block>>>(d_temp_s, d_bitmap);
    cudaEventRecord(stop, 0);
    cudaEventSynchronize(stop);
    cudaEventElapsedTime(&elapsedTime, start, stop);

    cudaMemcpy(bitmap, d_bitmap, sizeof(unsigned char) * DIM * DIM * 4, cudaMemcpyDeviceToHost);

    printf("CUDA ray tracing: %f sec\n", elapsedTime / 1000.0);
    printf("[%s] was generated.\n", argv[2]);

    // 이미지 파일 생성
    fprintf(fp, "P3\n");
    fprintf(fp, "%d %d\n", DIM, DIM);
    fprintf(fp, "255\n");
    for (int i = 0; i < DIM * DIM; i++) {
        fprintf(fp, "%d %d %d ", bitmap[4 * i], bitmap[4 * i + 1], bitmap[4 * i + 2]);
    }

    fclose(fp);
    cudaFree(d_bitmap);
    cudaFree(d_temp_s);
    free(bitmap);
    free(temp_s);

    return 0;
}

