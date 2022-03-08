package 코딩테스트연습.Hash;

import java.util.HashMap;
import java.util.Map;

public class phoneBook {
    public static void main(String[] args) {
        String[] phone_book = {"119", "97674223", "1195524421"};
        System.out.println(Solution.solution(phone_book));
        
    }
}

class Solution {
    public static boolean solution(String[] phone_book) {
        boolean answer = true;
        
       Map<String, String> map = new HashMap<>();
        for (String str : phone_book) {
            map.put(str, str);
        }

        for(int i = 0 ; i < phone_book.length ; i++) {
            for(int j = 1 ; j < phone_book[i].length() ; j++ )
                if(map.containsKey(phone_book[i].substring(0,j))) {
                    return false;  
            }          
        }
        return answer;
    }
}

/*
    phone_book 배열에 있는 값의 접두어가 중복되면 false, 접두어가 모두 다르다면 true 리턴.
    - ["123","456","789"] = true
    - ["12","123","1235","567","88"] = false

    Map의 Key 값을 비교하는데, for문 안에서 substring을 통해 값을 하나씩 하나씩 더해 가면서(1, 11, 119... 이렇게) 
    해당 값이 Map의 Key 값으로 존재하는지 확인한다.

    처음에는 Map의 Value 값으로 접두어만 추출한 다음 같은 이름의 Value 값이면
    하나의 길이로 인식하는 Set의 특성(중복된 값을 인식하지 않음)을 활용해서 phone_book 배열과 Set의 길이를 비교하는 방식으로 구현했다.
    그런데 테스트의 반은 맞고, 반은 통과하지 못했다.
*/