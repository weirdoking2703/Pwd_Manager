import java.util.Base64;

public class encode{
    public String encode(String pass) {
        String encodedString = Base64.getEncoder().encodeToString(pass.getBytes());
        //System.out.println("Encoded string: "+encodedString);
        //System.out.println(String.valueOf(encodedString));
        return (encodedString);
    }
}
