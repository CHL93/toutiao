package shu.chl.toutiao.Bean;

import java.util.HashMap;
import java.util.Map;

public class ViewObject {
    public Map<String,Object> objs=new HashMap<String,Object>();

    public Map<String, Object> getObjs() {
        return objs;
    }

    public void setObjs(Map<String, Object> objs) {
        this.objs = objs;
    }

    public void set(String key, Object value){
        objs.put(key,value);
    }
    public Object get(String key){
        return objs.get(key);
    }
}
