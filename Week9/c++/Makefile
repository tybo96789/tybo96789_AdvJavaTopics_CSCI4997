# This is a "Makefile". It is the C++ equivalent of build.xml for Java.

# define some variiables to reduce the amount of typing we need to do below
# You will probbaly need to change the location of the JDK to point to
# the installation on your machine.
#JDK=/usr/lib/jvm/default-java
JDK=/usr/java/jdk1.8.0_151
INCLUDE=-I$(JDK)/include -I$(JDK)/include/linux

# we define the binary "object" files as a variable to save typing below
# These are the C++ equivalent of Java's .class files.
#OBJECTS= edu_hpu_epier_jni_Demo.o edu_hpu_epier_jni_Wrapper.o Wrapped.o

OBJECTS= space_gameressence_atiburcio_tyler_Useful.o

#HEADERS= edu_hpu_epier_jni_Demo.h edu_hpu_epier_jni_Wrapper.h

HEADERS= space_gameressence_atiburcio_tyler_Useful.h

# This is the default action to take when you type "make"
# Our ultimate goal is to create the "shared object library.
# On windows you should change this to demo.dll
# On Mac you should change this to libdemo.dylib
all: libdemo.so

# we automatically generate a header file for the native methods
# in Demo.java by running the javah command. Note that we run this
# on the .class file, not the .java file.
######################
# edu_hpu_epier_jni_Demo.h: ../classes/edu/hpu/epier/jni/Demo.class
# 	javah -classpath ../classes edu.hpu.epier.jni.Demo
######################
space_gameressence_atiburcio_tyler_Useful.h: ../classes/space/gameressence/atiburcio/tyler/Useful.class
	javah -classpath ../classes space.gameressence.atiburcio.tyler.Useful

######################
# # this is the rule to compile the C++ code for the native method in Demo.java
# edu_hpu_epier_jni_Demo.o: edu_hpu_epier_jni_Demo.h edu_hpu_epier_jni_Demo.cc
# 	g++ -c edu_hpu_epier_jni_Demo.cc -D_REENTRANT -fPIC $(INCLUDE)
######################
# this is the rule to compile the C++ code for the native method in Demo.java
space_gameressence_atiburcio_tyler_Useful.o: space_gameressence_atiburcio_tyler_Useful.h space_gameressence_atiburcio_tyler_Useful.cc
	g++ -c space_gameressence_atiburcio_tyler_Useful.cc -D_REENTRANT -fPIC $(INCLUDE)

###########################################
# # this is the rule to make the header file for the native methods
# # in Wrapper.java
# edu_hpu_epier_jni_Wrapper.h: ../classes/edu/hpu/epier/jni/Wrapper.class
# 	javah -classpath ../classes edu.hpu.epier.jni.Wrapper
#
# # this is the rule to compile the C++ code for the native method in Wrapper.java
# edu_hpu_epier_jni_Wrapper.o: edu_hpu_epier_jni_Wrapper.h edu_hpu_epier_jni_Wrapper.cc
# 	g++ -c edu_hpu_epier_jni_Wrapper.cc -D_REENTRANT -fPIC $(INCLUDE)
#
# # This is the rule to compile the Wrapped class. It is pure C++
# Wrapped.o: Wrapped.h Wrapped.cc
# 	g++ -c Wrapped.cc -D_REENTRANT -fPIC
##########################################
# This is the rule to make the shared object library
libdemo.so: $(OBJECTS)
	g++ -shared -Wl,-soname,libdemo.so -o libdemo.so $(OBJECTS)

# If you run "make clean", this rule will delete all the generated files.
clean:
	rm -f $(OBJECTS) $(HEADERS)
