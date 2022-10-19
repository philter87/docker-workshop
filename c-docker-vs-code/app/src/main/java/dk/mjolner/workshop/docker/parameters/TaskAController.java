package dk.mjolner.workshop.docker.parameters;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static j2html.TagCreator.*;

@RestController
public class TaskAController {

    @GetMapping(value = "a", produces = MediaType.TEXT_HTML_VALUE)
    public String aPage() throws IOException {
        return Utils.page(
                style("font-family: 'Roboto', sans-serif;"),
                h1("1. Exercises: 'docker-run'"),
                h3("In this exercise you need to run this image 'philter87/docker-workshop' with different parameters. You can kill the application by clicking the link at the bottom of the page."),
                h3("The bullet points below are all red. When you use the correct parameters, they will turn green."),
                ul(
                        Utils.bullet("Run the container with an environment variable: 'MY_ENV=HelloWorld'. The current value is : \'" + getMyEnv()  + "\'", isEnvironmentVariablePresent()),
                        Utils.bullet("Limit to one cpu for the container. This is a way to limit the resources of container. Current cpu count: " + Runtime.getRuntime().availableProcessors(), isOneCpuCoreUsed()),
                        Utils.bullet("The current user in the docker container is: '" + getUserName() + "'. It is generally a bad idea to use the 'root' user inside a docker container. The image was created like this, but it is wrong. Try to run the container with the user 'app-user'", isUserEqualToAppUser()),
                        Utils.bullet("You can define a hostname for the container. Run the container with the hostname 'app-host'. The current hostname is '" + getHostName() +"'", isHostnameAppHost()),
                        Utils.bullet("Run the docker container in privileged mode. This is generally a bad idea, because it allows the container to access parts of the host OS. Privileged mode is however required when you want to run docker inside docker.", isContainerRunningInPriviledgedMode()),
                        Utils.bullet("There is a folder called 'shared-folder' in this docker-workshop repository. The shared folder should be mounted and mapped to the path /app/shared-folder in the container. You need to use absolute paths. The syntax is -v <absolute-path-on-host>:<absolute-path-in-container>. Hint: In powershell, you can use ${PWD} to get the current directory", isVolumeShared())
                ),
                div(a("Go back").withHref("/")),
                div(a("Kill the application").withHref("/kill"))
        );
    }

    private String getMyEnv(){
        return System.getenv("MY_ENV");
    }

    private boolean isEnvironmentVariablePresent() {
        var env = getMyEnv();
        System.out.println("MY_ENV=" + env);
        System.out.println("EnvironmentVariables: " + System.getenv());
        return env != null && env.equals("HelloWorld");
    }

    private boolean isHostnameAppHost() throws UnknownHostException {
        String hostName = getHostName();
        System.out.println("Hostname: " + hostName);
        return hostName.equals("app-host".toLowerCase());
    }

    private static String getHostName() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName().toLowerCase();
    }

    private boolean isOneCpuCoreUsed() {
        var cpuCount = Runtime.getRuntime().availableProcessors();
        System.out.println("CpuCount: " + cpuCount);
        return cpuCount == 1;
    }

    private static boolean isMemory1000MbOrBelow() {
        var maxMemory = Runtime.getRuntime().maxMemory();
        System.out.println("Current maxMemory:" + maxMemory);
        return maxMemory <= 2_100_000_000;
    }

    private static boolean isVolumeShared() throws IOException {
        System.out.println("Root ls: " + listFolders("."));
        System.out.println("App ls: " + listFolders(".", "app"));
        var path = Paths.get("app","shared-folder", "random-file.txt");
        return Files.isRegularFile(path);
    }

    private static String listFolders(String path, String... paths)  {
        try {
            return Files.list(Paths.get(path, paths)).map(Path::toString).collect(Collectors.joining(","));
        } catch (IOException e) {
            return "Path not found: " + path + paths;
        }
    }

    private boolean isContainerRunningInPriviledgedMode() {
        var path = Paths.get("dev", "mem");
        return Files.exists(path);
    }

    private boolean isUserEqualToAppUser(){
        var userName = getUserName();
        System.out.println("Current UserName: " + userName);
        return userName != null && userName.equals("app-user");
    }

    private static String getUserName() {
        return System.getProperty("user.name");
    }

}
