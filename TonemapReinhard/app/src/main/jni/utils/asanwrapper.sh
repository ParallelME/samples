{{{
ASAN_OPTIONS=debug=1,verbosity=1 \
LD_LIBRARY_PATH=/data/local/tmp/asan:$LD_LIBRARY_PATH \
LD_PRELOAD=libclang_rt.asan-arm-android.so \
exec $@
}}}
