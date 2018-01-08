import javassist.*;

import java.io.IOException;

/**
 * @version : 1.0
 * @Project : MagicBox
 * @Program Name  : PACKAGE_NAME.IedisCracker
 * @Class Name    : IedisCracker
 * @Copyright : Copyright (c)2017-2015
 * @Company : CreditEase
 * @Description :
 * @Author : tongwei
 * @Creation Date : 2018/1/8 19:06
 * @ModificationHistory Date         Author      Version            Description
 * ------------------------------------------------------------------
 * 2018/1/8      tongwei       1.0
 * 1.0 Version
 */
public class IedisCracker {
    private final static String IDEA_LIB="C:/Program Files/JetBrains/IntelliJ IDEA 2017.2.5/lib/*";
    private final static String IDEIS_LIB="C:/Users/paouke/.IntelliJIdea2017.2/config/plugins/Iedis/lib/*";
    public static void main(String[] args){
        try {
            ClassPool.getDefault().appendClassPath(IDEA_LIB);
            ClassPool.getDefault().appendClassPath(IDEIS_LIB);
            CtClass clazz = ClassPool.getDefault().getCtClass("com.seventh7.widget.iedis.a.p");
            CtMethod[] mds = clazz.getDeclaredMethods();
            for(CtMethod method : mds){
                if(method.getLongName().startsWith("com.seventh7.widget.iedis.a.p.f")){
                    System.out.println("Inject :: SUCCESS!");
                    try {
                        method.insertBefore("if(true){return true;} ");
                    } catch (CannotCompileException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
            }
            clazz.writeFile("/tmp/p.class");
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}