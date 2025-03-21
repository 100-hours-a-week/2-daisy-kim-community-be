package daisy.community_be.controller;

import daisy.community_be.service.HelloService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
    private final HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @GetMapping("hello")    // '/hello' 경로로 GET 요청이 들어오면 메서드 실행
    public String hello(Model model) {
        String message = helloService.getHelloMessage();
        // model 객체를 이용해서 HTML 템플릿에 데이터 전달 (hello.html에서 ${data}으로 호출)
        model.addAttribute("data", "Hello World!");
        return "hello"; // templates 폴더 hello.html 파일을 찾기

    }
}
