package com.sourceforge.apklib.foundation.data;
import android.app.Activity;
import android.support.v4.util.ArrayMap;
import android.view.View;
import java.lang.reflect.Field;

/**
 * 视图绑定
 */
public class ViewBinding {
    /**
     * R文件id映射
     */
    public static final ArrayMap<String,ArrayMap<String,Integer>> R_FILE_FIELD_MAP
            =new ArrayMap<String,ArrayMap<String,Integer>>();

    public interface ViewFinder{
        /**
         * 返回视图绑定
         * @param viewId 视图ID
         * @param <T>
         * @return
         */
        <T> T findViewById(int viewId);
    }

    /**
     * 获取Activity的ViewFinder
     * @param activity
     * @return
     */
    public static final ViewFinder getActivityViewFinder(final Activity activity){
        return new ViewFinder() {
            @Override
            public <T> T findViewById(int viewId) {
                return (T)activity.findViewById(viewId);
            }
        };
    }

    public static final ViewFinder getViewFinder(final View view){
        return new ViewFinder() {
            @Override
            public <T> T findViewById(int viewId) {
                return (T)view.findViewById(viewId);
            }
        };
    }
    /**
     * 绑定视图中的field,field的类型必须为View
     * @param obj 要绑定的视图类
     * @param RFile 从哪个r文件获取
     * @param finder 调用谁的findViewById
     */
    public static final void binding(Object obj,Class<?> RFile,ViewFinder finder){
        ArrayMap<String,Integer> rfnl=reflectResIdNames(RFile);
        Field[] fields=obj.getClass().getDeclaredFields();
        if(fields!=null&&fields.length>0){
            for(Field field:fields){
                if(isExtendsByClass(field.getType(),View.class)){
                    Integer findId=rfnl.get(field.getName());
                    if(findId!=null) {
                        View view = finder.findViewById(findId);
                        field.setAccessible(true);
                        try {
                            field.set(obj, view);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * 判断类是否从某一个类继承
     * @param currClass 要判定的类
     * @param superClass 指定的父类
     * @return
     */
    private static final boolean isExtendsByClass(Class<?> currClass,Class<?> superClass){
        if(currClass==superClass)return true;
        Class<?> parent=currClass.getSuperclass();
        if(parent==null)return false;
        return isExtendsByClass(parent, superClass);
    }
    /**
     * 获取R文件中的ID字段名称
     * @param r
     * @return
     */
    private static final ArrayMap<String,Integer> reflectResIdNames(Class<?> r){
        String name=r.getName();
        synchronized (R_FILE_FIELD_MAP) {
            try{
                if(R_FILE_FIELD_MAP.get(name)==null){
                    ArrayMap<String,Integer> idMap=new ArrayMap<String,Integer>();
                    R_FILE_FIELD_MAP.put(name, idMap);
                    Object obj=Class.forName(name);
                    Field[] fields=Class.forName(name+"$id").getDeclaredFields();
                    if(fields!=null&&fields.length>0){
                        for(Field field:fields){
                            try {
                                idMap.put(field.getName(),field.getInt(obj));
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }catch(ClassNotFoundException e){
                e.printStackTrace();
            }
            return R_FILE_FIELD_MAP.get(name);
        }
    }

}
