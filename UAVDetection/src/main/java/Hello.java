/**
 * Created by zhoumeng on 10/21/17.
 */
public class Hello {
    public native void say(String ss);

    public static void main(String[] args) {
        System.load("/home/hadoop/zm/new-jni/JNITest/JNITest/src/libnewapi.so");
        Hello hello = new Hello();
        hello.say("2");
    }
}
