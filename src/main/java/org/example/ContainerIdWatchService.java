package org.example;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.WatchService;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.StandardWatchEventKinds;
import java.util.List;
import java.util.Properties;

public class ContainerIdWatchService {
    private final Path pathToWatch;
    private final WatchService watchService;
    private final boolean watchSingleFile;
    private final Path fileToWatch;
    private static final String FilePathName = "/Users/jaeyoung/termination-log";

    public ContainerIdWatchService(String path, boolean watchSingleFile) throws IOException {
        this.pathToWatch = Paths.get(path);
        this.watchService = FileSystems.getDefault().newWatchService();
        this.watchSingleFile = watchSingleFile;

        if (watchSingleFile) {
            // 파일이 위치한 디렉토리를 감시 대상으로 등록.
            this.fileToWatch = this.pathToWatch;
            this.fileToWatch.getParent().register(
                    watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
        } else {
            // 전체 디렉토리를 감시 대상으로 등록합니다.
            this.fileToWatch = null;
            this.pathToWatch.register(watchService,
                    StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
        }
    }

    public void processEvents() {
        while (true) {
            try {
                WatchKey key = watchService.take(); // 이벤트 대기
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    Path changed = (Path) event.context();

                    if (watchSingleFile) {
                        // 변경된 파일의 전체 경로를 얻습니다.
                        Path changedAbsolutePath = this.pathToWatch.getParent().resolve(changed);

                        // 특정 파일의 변경사항인지 확인합니다.
                        if (changedAbsolutePath.equals(this.fileToWatch) && kind.equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
                            handleFileChange(changedAbsolutePath, kind);
                        }
                    } else {
                        handleDirectoryChange(this.pathToWatch.resolve(changed), kind);
                    }
                }
                key.reset(); // 키 리셋
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 예외 발생 시 스레드 중단
                System.out.println("stop watcher_error: " + e.getMessage());
                break;
            } catch (IOException e){
                System.out.println("IO Exception: " + e.getMessage());
                break; // IO 예외 발생 시 루프 종료
            }
        }
    }

    private void handleFileChange(Path file, WatchEvent.Kind<?> kind) throws IOException {
        Container container = new Container(); // Container 객체 생성
        System.out.println("파일 변경 감지: " + file + " (" + kind.name() + ")");
        try {
            List<String> lines = Files.readAllLines(file);
            for (String line : lines) {
                if (line.contains("whatap")) {
                    String[] parts = line.split(":");

                    // parts의 길이가 2일떄 backup 내용이 없으므로 containerId 값만 설정
                    if (parts.length >= 2) {
                        String containerId = parts[1].trim();
                        container.setContainerId(containerId); // Container 객체에 ContainerId 설정

                        // parts 배열의 길이가 3 이상이라면 backup 내용 존재
                        if (parts.length ==3){
                            String terminationMessage = parts[2].trim();
                            if (!terminationMessage.isEmpty()) { // backup 값이 있을 경우에만 처리합니다.
                                System.out.println("terminationMessage="+terminationMessage);
                                Files.write(file, terminationMessage.getBytes(StandardCharsets.UTF_8), StandardOpenOption.WRITE);
                            }
                        }
                    }
                    System.out.println("ContainerId="+container.getContainerId());
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        } catch (IOException e) {
            System.out.println("error="+e.getMessage());
        }
    }
    private void handleDirectoryChange(Path file, WatchEvent.Kind<?> kind) {
        System.out.println("디렉토리 내 변경 감지: " + file + " (" + kind.name() + ")");
    }

    public static void main(String[] args) throws IOException {
        String envValue =  System.getenv("GET_CONTAINER_ID_BY_INJECTION");
        boolean getContainerIdByInjection = "true".equalsIgnoreCase(envValue);
        getContainerIdByInjection = true;
        System.out.println("getContainerIdByInjection="+getContainerIdByInjection);
        System.out.println("FilePathName="+FilePathName);

        //GET_CONTAINER_ID_BY_INJECTION 환경변수가 있을 때만 실행
        if (!getContainerIdByInjection){
            return;
        }

        // FilePathName으로 등록된 /dev/termination-log 파일 존재 여부 확인
        if (!Files.exists(Paths.get(FilePathName))){
            System.out.println("FileNoutFound:"+FilePathName);
            return;
        }

        // FilePathName 으로 설정한 특정 파일에 대한 변경만 감지
        // /dev/termination-log 파일만 감지하도록 한다.
        ContainerIdWatchService singleFileWatcher = new ContainerIdWatchService(FilePathName, true);
        new Thread(singleFileWatcher::processEvents).start();

//         전체 디렉토리에 대한 변경을 감지:
//        FileAndDirectoryWatchService directoryWatcher =
//                new FileAndDirectoryWatchService("/Users/jaeyoung/test", false);
//        new Thread(directoryWatcher::processEvents).start();
    }
}