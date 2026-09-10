import java.util.Scanner;

public class Chat {
    public static void main(String[] args){

        String[] repliesToIllegalRequestBot1 = {"say say before <request>", "say what? <request>!? what's <request>"};
        String[] repliesToLegalRequestsBot1 = {"say <phrase>? ok: <phrase>", "sure: <phrase>"};
        String[] repliesToIllegalRequestBot2 = {"what?? <request>!", "what do you mean <request>?"};
        String[] repliesToLegalRequestsBot2 = {"your request is legal so <phrase>", "ok, <phrase>", " <phrase>"};
        String bot1Name = "Elias";
        String bot2Name = "Michel";
        ChatterBot[] bots = {new ChatterBot(bot1Name , repliesToLegalRequestsBot1 ,repliesToIllegalRequestBot1),
                            new ChatterBot(bot2Name , repliesToLegalRequestsBot2 ,repliesToIllegalRequestBot2)};

        String statement = "say hello";
        Scanner scanner = new Scanner(System.in);
        for(int i = 0 ;; i = (i + 1) % bots.length){
            statement = bots[i].replyTo(statement);
            System.out.println(bots[i].getName() + ": " +  statement);
            scanner.nextLine();
        }






        //ChatterBot bot1 = new ChatterBot("botty", new String[]{"whatt"}, new String[]{ "adad" });
        //String x = bot1.replyTo("something");
        //System.out.println(x);
    }
}
