package com.jpndev.player.utils;

public class ClassC {

 static ClassC temp;
 public static ClassC getInstances()
 {
    if(temp==null)
     {
      temp=new ClassC();
      }
   return temp;
 }

 public int getInt()
 {

  return 10;
 }
 public ClassC(){

 }

 public  long pos =0;

}
