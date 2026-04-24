package utilities.Common;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;

public class Container {
    public jade.wrapper.AgentContainer getContainer(){
        ProfileImpl profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        //profile.setParameter(Profile.MAIN_HOST, "192.168.4.2");
        profile.setParameter(Profile.GUI, "false");

        // 返回一个容器来运行代理
        return Runtime.instance().createMainContainer(profile);
    }
}
