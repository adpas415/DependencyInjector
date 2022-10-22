package adp.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DependencyInjector {

    private record PortAdapterDetails(Class dependency, Class<?>... constructorVars) {
        <E> E constructAdapter(Object... args) {
            try {
                return (E) dependency.getDeclaredConstructor(constructorVars).newInstance(args);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static Map<Class, PortAdapterDetails> AdapterDetailsViaInterfaceClass = new ConcurrentHashMap<>();

    public static <E> void setPortAdapter(Class<E> portInterface, Class<? extends E> adapterClass, Class<?>... adapterConstructorVars) {
        AdapterDetailsViaInterfaceClass.put(portInterface, new PortAdapterDetails(adapterClass, adapterConstructorVars));
    }

    public static <E> E getPortAdapter(Class<E> classInterface, Object... constructionVars) {

        if(!AdapterDetailsViaInterfaceClass.containsKey(classInterface))
            throw new RuntimeException("There is no assigned Port Adapter Class for the Interface " + classInterface.getSimpleName());

        PortAdapterDetails adapterDetails = AdapterDetailsViaInterfaceClass.get(classInterface);

        return adapterDetails.constructAdapter(constructionVars);

    }

}
