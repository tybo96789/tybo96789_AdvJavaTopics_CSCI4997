#include <jni.h>
#include <cstring>
#include <iostream>
#include "space_gameressence_atiburcio_tyler_Useful.h"
 /*
  * Class:     space_gameressence_atiburcio_tyler_Useful
  * Method:    method
  * Signature: (Ljava/lang/String;)V
  */
 JNIEXPORT void JNICALL Java_space_gameressence_atiburcio_tyler_Useful_method
   (JNIEnv * env , jclass, jstring s)
  {

    jboolean is_copy;
    //Load the Java String as chars
    const char* cs = env->GetStringUTFChars(s, &is_copy);
    //Print the chars to standard output
    std::cout <<"C++: string arg= "<<cs<<std::endl;
    //Print the length of the String
    std::cout <<"Length of the string: "<<strlen(cs)<<std::endl;
    //Wipe memory space that was used by the chars
    env->ReleaseStringUTFChars(s, cs);
  }
