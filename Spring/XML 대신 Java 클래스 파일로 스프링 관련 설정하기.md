# XML 대신 Java 클래스 파일로 스프링 관련 설정하기

Eclipse의 Spring Legacy Project를 통해 Spring MVC 프로젝트를 생성하면 XML 기반으로 스프링 관련 설정을 하도록 되어있다. 스프링 3버전 이후부터 XML 대신 Java 클래스 파일로 스프링 관련 설정을 할 수 있도록 변경되었다. 

이 글에서 Java 클래스 파일로 대체할 XML 파일은 다음과 같다.

참고로 이 글은 스프링 5버전, JDK 1.8 기준으로 작성되었다.

- web.xml

- servlet-context.xml
- root-context.xml

## web.xml

아래는 Eclipse를 통한 Spring MVC 프로젝트 생성 시 보이는 web.xml 설정이다.

```
<?xml version="1.0" encoding="UTF-8"?>
<web-app version="2.5" xmlns="http://java.sun.com/xml/ns/javaee"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://java.sun.com/xml/ns/javaee https://java.sun.com/xml/ns/javaee/web-app_2_5.xsd">

	<!-- The definition of the Root Spring Container shared by all Servlets and Filters -->
	<context-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>/WEB-INF/spring/root-context.xml</param-value>
	</context-param>
	
	<!-- Creates the Spring Container shared by all Servlets and Filters -->
	<listener>
		<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
	</listener>

	<!-- Processes application requests -->
	<servlet>
		<servlet-name>appServlet</servlet-name>
		<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
		<init-param>
			<param-name>contextConfigLocation</param-name>
			<param-value>/WEB-INF/spring/appServlet/servlet-context.xml</param-value>
		</init-param>
		<load-on-startup>1</load-on-startup>
	</servlet>
		
	<servlet-mapping>
		<servlet-name>appServlet</servlet-name>
		<url-pattern>/</url-pattern>
	</servlet-mapping>

</web-app>
```

위 설정 내용을 Java로 변경하면 아래와 같다.

클래스 파일은 src/main/java 아래에 원하는 이름으로 생성 후 관리하면 된다.

아래 클래스에 사용된 메소드는 AbstractAnnotationConfigDispatcherServletInitializer 상속 후 오버라이딩 하면 된다.

```
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebConfig extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { root-context.xml을 대신하는 클래스를 지정 };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { servlet-context.xml을 대신하는 클래스를 지정 };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] {"/"}; // 기본 경로 지정
	}

}
```

## servlet-context.xml

아래는 Eclipse를 통한 Spring MVC 프로젝트 생성 시 보이는 servlet-context.xml 설정이다.

```
<?xml version="1.0" encoding="UTF-8"?>
<beans:beans xmlns="http://www.springframework.org/schema/mvc"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:beans="http://www.springframework.org/schema/beans"
	xmlns:context="http://www.springframework.org/schema/context"
	xsi:schemaLocation="http://www.springframework.org/schema/mvc https://www.springframework.org/schema/mvc/spring-mvc.xsd
		http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd
		http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">

	<!-- DispatcherServlet Context: defines this servlet's request-processing infrastructure -->
	
	<!-- Enables the Spring MVC @Controller programming model -->
	<annotation-driven />

	<!-- Handles HTTP GET requests for /resources/** by efficiently serving up static resources in the ${webappRoot}/resources directory -->
	<resources mapping="/resources/**" location="/resources/" />

	<!-- Resolves views selected for rendering by @Controllers to .jsp resources in the /WEB-INF/views directory -->
	<beans:bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
		<beans:property name="prefix" value="/WEB-INF/views/" />
		<beans:property name="suffix" value=".jsp" />
	</beans:bean>
	
	<context:component-scan base-package="스프링 빈(Bean) 생성 관련 클래스 스캔하는 패키지 지정" />
	
	
	
</beans:beans>
```

위 설정 내용을 Java로 변경하면 아래와 같다.

```
@EnableWebMvc
public class ServletConfig implements WebMvcConfigurer {
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {

		InternalResourceViewResolver bean = new InternalResourceViewResolver();
		bean.setViewClass(JstlView.class);
		bean.setPrefix("/WEB-INF/views/");
		bean.setSuffix(".jsp");
		registry.viewResolver(bean);
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

}
```

## root-context.xml

아래는 Eclipse를 통한 Spring MVC 프로젝트 생성 시 보이는 root-context.xml 설정이다.

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd">
	
	<!-- Root Context: defines shared resources visible to all other web components -->
		
</beans>
```

위 설정 내용을 Java로 변경하면 아래와 같다.

```
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootConfig {

}
```