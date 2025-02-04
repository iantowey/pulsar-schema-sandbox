package com.it.sandbox.pulsar;

import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.Messages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class ConsumerBatchMessages<T> implements Messages<T> {
    private List<Message<T>> messageList = new ArrayList<>();

    @Override
    public int size() {
        return this.messageList.size();
    }

    void add(Message<T> message) {
        this.messageList.add(message);
    }

    @Override
    public Iterator<Message<T>> iterator() {
        return this.messageList.iterator();
    }
}