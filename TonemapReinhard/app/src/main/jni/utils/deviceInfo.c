#include <stdio.h>

#ifdef __APPLE__
#include <OpenCL/cl.h>
#else
#include <CL/cl.h>
#endif


void deviceInfo() {
   /* Host/device data structures */
   cl_platform_id platform;
   cl_uint ndev;
   cl_uint num_platforms;
   cl_device_id *dev;
   cl_uint addr_data;
   cl_int err;
   cl_uint i;

   /* Extension data */
   char name_data[48], ext_data[4096];

   err = clGetPlatformIDs(1, NULL, &num_platforms);
    printf("Platforms: %d\n", num_platforms);


   /* Identify a platform */
   err = clGetPlatformIDs(1, &platform, NULL);
   if(err < 0) {
        printf("Couldn't find any platforms.");
      exit(1);
   }

   /* Access a device, preferably a GPU */
   /* Changed on 2/12 to fix the CL_INVALID_VALUE error */
   err = clGetDeviceIDs(platform, CL_DEVICE_TYPE_ALL, 0, NULL, &ndev);
   printf("Devices: %d\n\n", ndev);

   dev = malloc(ndev * sizeof(*dev));
   err = clGetDeviceIDs(platform, CL_DEVICE_TYPE_ALL, ndev, dev, NULL);
   if(err < 0) {
      printf("Couldn't access any devices. %d\n", ndev);
      exit(1);
   }

   for(i = 0; i < ndev; ++i) {
       /* Access device name */
       err = clGetDeviceInfo(dev[i], CL_DEVICE_NAME,
          48 * sizeof(char), name_data, NULL);
       if(err < 0) {
          printf("Couldn't read extension data.\n");
          exit(1);
       }

       /* Access device address size */
       clGetDeviceInfo(dev[i], CL_DEVICE_ADDRESS_BITS,
          sizeof(addr_data), &addr_data, NULL);

       /* Access device extensions */
       clGetDeviceInfo(dev[i], CL_DEVICE_EXTENSIONS,
          4096 * sizeof(char), ext_data, NULL);

       printf("NAME: %s\nADDRESS_WIDTH: %u\nEXTENSIONS: %s\n\n",
                name_data, addr_data, ext_data);
   }
}

int main() {
    deviceInfo();
    return 0;
}
