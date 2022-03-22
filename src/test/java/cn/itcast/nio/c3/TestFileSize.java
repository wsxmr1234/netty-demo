package cn.itcast.nio.c3;

import lombok.SneakyThrows;

import java.io.FileInputStream;

public class TestFileSize {
    @SneakyThrows
    public static void main(String[] args) {
        System.out.println(new FileInputStream("data.txt").getChannel().size());
    }
}
