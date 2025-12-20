public class ServiceImpl implements Service {
    @Override
    public String sendMessage(String clientMessage) {
        System.out.println(clientMessage);
        return "Server response";
    }
}
