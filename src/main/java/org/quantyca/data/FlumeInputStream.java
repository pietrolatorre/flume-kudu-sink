package org.quantyca.data;

import java.io.IOException;
import java.io.InputStream;

import org.apache.flume.serialization.ResettableInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlumeInputStream extends InputStream {
    private static final Logger logger = LoggerFactory.getLogger(FlumeInputStream.class);

    private final ResettableInputStream in;

    public FlumeInputStream(ResettableInputStream input) {
        this.in = input;
    }

    @Override
    public int read() throws IOException {
        try {
            return this.in.read();
        } catch (Exception e) {
            logger.error("input stream read failed:" + e.getMessage());
            return 0;
        }
    }

}