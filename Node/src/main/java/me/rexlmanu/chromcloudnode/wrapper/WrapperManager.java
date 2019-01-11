package me.rexlmanu.chromcloudnode.wrapper;

import com.google.common.collect.Lists;
import lombok.Getter;
import me.rexlmanu.chromcloudcore.wrapper.Wrapper;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Getter
public final class WrapperManager {

    private List<Wrapper> wrappers;

    public WrapperManager() {
        this.wrappers = Lists.newArrayList();
    }

    public void disconnect(Wrapper wrapper, boolean closeChannel) {
        if (closeChannel) {
            wrapper.getChromChannelSender().getChannel().closeFuture();
        }

        //todo handle servers

        this.wrappers.remove(wrapper);
    }

    public Wrapper getWrapperWithLowestUse() {
        AtomicReference<Wrapper> wrapper = new AtomicReference<>();
        int onlineServers = Integer.MAX_VALUE;
        this.wrappers.forEach(tempWrapper -> {
            if (tempWrapper.getServers().size() < onlineServers)
                wrapper.set(tempWrapper);
        });
        return wrapper.get();
    }

}
