package com.jing.jhttp.request;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kidbei on 16-1-15.
 */
public abstract class AbstractPool {

    protected final static ExecutorService pool = Executors.newFixedThreadPool(3);

}
