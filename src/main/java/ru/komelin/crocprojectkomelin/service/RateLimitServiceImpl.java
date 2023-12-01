package ru.komelin.crocprojectkomelin.service;

import org.springframework.stereotype.Service;
import ru.komelin.crocprojectkomelin.model.Link;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitServiceImpl implements RateLimitService{
    private final Map<String, Integer> linkWithCount = new ConcurrentHashMap<>();

    private void refresh(Link link) {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                linkWithCount.remove(link.getLinkAddress());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public boolean isAccessGained(Link link) {
        if (!linkWithCount.containsKey(link.getLinkAddress())) {
            linkWithCount.put(link.getLinkAddress(), link.getRequestPerSecond() - 1);
            refresh(link);
            return true;
        } else if (linkWithCount.get(link.getLinkAddress()) > 0) {
            linkWithCount.put(link.getLinkAddress(), linkWithCount.get(link.getLinkAddress()) - 1);
            return true;
        } else {
            return false;
        }
    }
}
