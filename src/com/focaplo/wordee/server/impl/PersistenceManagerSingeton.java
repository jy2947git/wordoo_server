package com.focaplo.wordee.server.impl;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public class PersistenceManagerSingeton {
    private static final PersistenceManagerFactory pmfInstance =
        JDOHelper.getPersistenceManagerFactory("transactions-optional");

    private PersistenceManagerSingeton() {}

    public static PersistenceManagerFactory instance() {
        return pmfInstance;
    }

}
