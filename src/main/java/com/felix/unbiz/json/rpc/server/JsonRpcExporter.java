package com.felix.unbiz.json.rpc.server;

/**
 * ClassName: JsonRpcExporter <br/>
 * Function: 服务端暴露的json rpc服务封装
 *
 * @author wangxujin
 */
public class JsonRpcExporter implements ServiceNameAware {

    /**
     * 服务接口名称
     */
    private String serviceInterfaceName;

    /**
     * 服务实现类
     */
    private Object serviceBean;

    /**
     * Creates a new instance of JsonRpcExporter.
     */
    public JsonRpcExporter() {

    }

    /**
     * Creates a new instance of JsonRpcExporter.
     *
     * @param serviceInterfaceName
     * @param serviceBean
     */
    public JsonRpcExporter(String serviceInterfaceName, Object serviceBean) {
        this.serviceInterfaceName = serviceInterfaceName;
        this.serviceBean = serviceBean;
    }

    /**
     * 通过反射获取接口class
     *
     * @return 接口的class
     */
    public Class<?> getServiceInterface() {
        try {
            return Class.forName(serviceInterfaceName);
        } catch (Exception e) {
            throw new RuntimeException("Class not found for " + serviceInterfaceName);
        }
    }

    /**
     * 获取服务名称
     *
     * @return 名称
     */
    @Override
    public String getName() {
        return this.getServiceInterface().getSimpleName();
    }

    public String getServiceInterfaceName() {
        return serviceInterfaceName;
    }

    public void setServiceInterfaceName(String serviceInterface) {
        this.serviceInterfaceName = serviceInterface;
    }

    public Object getServiceBean() {
        return serviceBean;
    }

    public void setServiceBean(Object serviceBean) {
        this.serviceBean = serviceBean;
    }

}
