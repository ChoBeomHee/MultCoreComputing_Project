#include <iostream>
#include <thrust/transform_reduce.h>
#include <thrust/functional.h>
#include <thrust/device_vector.h>
#include <thrust/host_vector.h>
#ifdef __CUDA_ARCH__
__device__ long num_steps = 1000000000;
#else
long num_steps = 1000000000;
#endif

struct cal {
  double x;
  __device__ __host__ double operator()(int i) { // float에서 double로 수정
    double step = 1.0 / (double) num_steps;
    x = (i+0.5) * step;
    return 4.0/(1.0+x*x);
  }
};

float cal_pie(const thrust::device_vector<int> &x){
   size_t N = x.size();
   thrust::device_vector<float> vec_temp(N);
   thrust::transform(x.begin(), x.end(), vec_temp.begin(), cal());
   return thrust::reduce(vec_temp.begin(), vec_temp.end());
}

int main() {
  thrust::device_vector<int> D(num_steps);
  thrust::sequence(D.begin(), D.end());
  //  for (int i = 0; i < D.size(); i++)
  //  std::cout << "D[" << i << "] = " << D[i] << std::endl;

  double pi = cal_pie(D);
	printf("pi=%.10lf\n",pi / (double)num_steps);

 // std::cout << "Sum of squares: " << cal_pie(D) / num_steps << std::endl;

  return 0;
}
