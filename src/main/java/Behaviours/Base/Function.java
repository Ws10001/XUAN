package Behaviours.Base;

import Agents.BaseAgent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;

import java.lang.reflect.Field;

public abstract class Function extends TickerBehaviour {
    private BaseAgent agent;
    public Function(BaseAgent agent){
        super(agent,1000);
        this.agent = agent;
    }


    /**
     * 该方法可以实现将一个标志位设置为true以启动某行为，并等待该标志位重置为false。
     * @param fieldName 要设置为 true 的标志位字段的名称。
     */
    public void ActionUnitWait(String fieldName){
        System.out.println("标志位:"+fieldName+"被修改！");
        SetObjectVariable(this.agent,fieldName,true);
        while ((boolean)GetObjectVariable(this.agent,fieldName)) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 该方法可以实现将一个标志位设置为true以启动某行为。
     * @param fieldName 要设置为 true 的标志位字段的名称。
     */
    public void ActionUnit(String fieldName){
        SetObjectVariable(this.agent,fieldName,true);
    }

    public void CombinedActionUnitWait(String fieldName_a,String fieldName_b){
        System.out.println("联合行为标志位:"+fieldName_a+"以及"+fieldName_b+"被修改！");
        SetObjectVariable(this.agent,fieldName_a,true);
        try {
            // 延时 2 秒（2000 毫秒）
            Thread.sleep(2000);     // 约定好行为a启动后2s启动行为b
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SetObjectVariable(this.agent,fieldName_b,true);
        while ((boolean)GetObjectVariable(this.agent,fieldName_a)||(boolean)GetObjectVariable(this.agent,fieldName_b)) {
            try {
                System.out.printf("等待联合标志位重置:"+(boolean)GetObjectVariable(this.agent,fieldName_a)+"以及"+(boolean)GetObjectVariable(this.agent,fieldName_b)+"%n");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 该方法可以实现对一个标志位进行保护性检测，通常用于可能被多种Function层调用的底层行为，为保证同时仅执行一个底层，需要等待该标志位为false时才可以执行下面的操作。
     * @param fieldName 要保护的标志位字段的名称。
     */
    public void ActionProtection(String fieldName){
        while ((boolean)GetObjectVariable(this.agent,fieldName)) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Object GetObjectVariable(Object obj, String fieldName) {
        try {
            // 获取对象的类类型
            Class<?> objClass = obj.getClass();
            // 遍历字段名列表，打印对应字段的值
            Field field = objClass.getDeclaredField(fieldName);  // 根据字段名获取字段
            field.setAccessible(true); // 确保可以访问 private 字段
            // 获取字段值并打印
            return field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return "";

    }
    private void SetObjectVariable(Object obj, String fieldName,Object value) {
        try {
            // 获取对象的类类型
            Class<?> objClass = obj.getClass();
            // 遍历字段名列表，打印对应字段的值
            Field field = objClass.getDeclaredField(fieldName);  // 根据字段名获取字段
            field.setAccessible(true); // 确保可以访问 private 字段
            field.set(obj,value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
