# Singleton

Singleton 패턴은 클래스의 인스턴스가 메모리 상에 하나만 존재할 수 있도록 하는 동시에 해당 인스턴스를 어디에서나 접근할 수 있도록 하는 패턴이다.  

예를 들어 APP을 사용하는데, 원하는 기능을 자신이 이동하는 페이지마다 적용되게 하고 싶다면 하나의 객체만 생성해서 계속 사용해야 할 것이다.  
아래 코드는 **다크모드** 그리고 **폰트 사이즈 조절** 이라는 기능이 있다고 가정하고, 첫번째 페이지(FirstPage)에서 적용한 다크모드와 폰트 사이즈 내용이 두번째 페이지(SecondPage)에서도 유지되는지 확인해본다.  

```
public class Settings {

    private boolean darkMode = false;
    private int fontSize = 13;

    public boolean getDarkMode() {
        return darkMode;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setDarkMode(boolean _darkMode) {
        darkMode = _darkMode;
    }

    public void setFontSize(int _fontSize) {
        fontSize = _fontSize;
    }
    
}

public class FirstPage {

    private Settings settings = new Settings();

    public void setAndPrintSettings() {
        settings.setDarkMode(true);
        settings.setFontSize(15);
    
        System.out.println(settings.getDarkMode() + " / " + settings.getFontSize());
    }
    
}

public class SecondPage {
    
    private Settings settings = new Settings();

    public void printSettings() {
        System.out.println(settings.getDarkMode() + " / " + settings.getFontSize());
    }
}

public class MyProgram {
    public static void main(String[] args) {
        new FirstPage().setAndPrintSettings();
        new SecondPage().printSettings();
    }
    
}

결과)
true / 15
false / 13
```

결과를 확인해보니 첫번째, 두번째 페이지에 각각 다른 내용이 출력된다. 즉 첫번째, 두번째 페이지 클래스에 생성한 Settings 인스턴스는 각각 다른 인스턴스라는 것을 의미한다.  

위 코드를 Singleton 형태를 가지도록 리팩토링한다.  

```
public class Settings {

    // 생성자를 private으로 만들어 다른 클래스에서 new 키워드로 새로운 인스턴스를 생성하지 못하도록 한다.
    private Settings() {}; 

    // 클래스 자기 자신인 Settings를 가지는 static 변수를 만들고 null로 초기화한다.
    private static Settings settings = null;

    // 정적 메소드 getSettings() 
    public static Settings getSettings() {
        if (settings == null) {
            settings = new Settings();
        }
        return settings;
    }

    private boolean darkMode = false;
    private int fontSize = 13;

    public boolean getDarkMode() {
        return darkMode;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setDarkMode(boolean _darkMode) {
        darkMode = _darkMode;
    }

    public void setFontSize(int _fontSize) {
        fontSize = _fontSize;
    }
    
}
```

*클래스 안에 static이 아닌 변수나 메소드들은 객체가 생성될 때마다 메모리의 공간을 새로 차지한다.*   
*static으로 선언된 것들은 객체가 얼마나 만들어지든 메모리의 지정된 공간에 딱 하나씩만 존재한다.*

FirstPage, SecondPage 클래스에서 Settings 객체를 가져오는 부분을 아래와 같이 수정한다.
Settings 클래스의 정적(static) 메소드인 getSettings()를 호출한다. 이 메소드는 객체가 생성된 이력이 없으면 객체를 생성하고(new Settings()), 이미 생성된 객체가 있다면 그 객체를 반환하는 메소드이다.

```
public class FirstPage {

    private Settings settings = Settings.getSettings();

    public void setAndPrintSettings() {
        settings.setDarkMode(true);
        settings.setFontSize(15);
    
        System.out.println(settings.getDarkMode() + " / " + settings.getFontSize());
    }
    
}

public class SecondPage {
    
    private Settings settings = Settings.getSettings();

    public void printSettings() {
        System.out.println(settings.getDarkMode() + " / " + settings.getFontSize());
    }
}
```

수정 후 결과를 출력해본다.
FirstPage 클래스에서 변경된 내용이 SecondPage에서도 똑같이 출력된 것을 확인할 수 있다.  
FirstPage 클래스에서 정적 메소드 getSettings()를 통해 객체 생성 후 작업을 했고, SecondPage 클래스는 FirstPage에서 생성된 객체를 그대로 가져다 사용하는 모양이 된다.

```
public class MyProgram {
    public static void main(String[] args) {
        new FirstPage().setAndPrintSettings();
        new SecondPage().printSettings();
    }
    
}
결과)
true / 15
true / 15
```



# 출처
* [객체지향 디자인패턴](https://www.youtube.com/watch?v=lJES5TQTTWE)