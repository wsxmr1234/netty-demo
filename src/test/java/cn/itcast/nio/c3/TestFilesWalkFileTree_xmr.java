package cn.itcast.nio.c3;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class TestFilesWalkFileTree_xmr {
    // 复制文件夹
    public static void main(String[] args) throws IOException {
        String source = "E:\\TestFiles";
        String target = "E:\\TestFiles1";

        Files.walkFileTree(Paths.get(source), new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                String targetName = dir.toString().replace(source, target);
                Files.createDirectory(Paths.get(targetName));

                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                String targetName = file.toString().replace(source, target);
                Files.copy(file, Paths.get(targetName));
                return super.visitFile(file, attrs);
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return super.visitFileFailed(file, exc);
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return super.postVisitDirectory(dir, exc);
            }
        });

    }

}
