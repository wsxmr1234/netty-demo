package cn.itcast.nio.xmr_t2;

import java.lang.annotation.Target;
import java.nio.ByteBuffer;
import java.util.Spliterator;

import static cn.itcast.nio.c2.ByteBufferUtil.debugAll;

public class TestByteBufferExam {
    public static void main(String[] args) {
        ByteBuffer source = ByteBuffer.allocate(32);
        source.put("Hello, world\nI'm BIG SB\nHAH".getBytes());
        split(source);
        source.put("AHAHAHAHAHAHAHAHAH\n".getBytes());
        split(source);

    }

    private static void split(ByteBuffer source) {
        source.flip();
        for (int i = 0; i < source.limit() ; i++) {
            if (source.get(i) == '\n') {
                int length = i + 1 - source.position();
                ByteBuffer target = ByteBuffer.allocate(length);
                for (int i1 = 0; i1 < length; i1++) {
                    target.put(source.get());
                }
                debugAll(target);
            }
        }
        source.compact();
    }
}
