import java.util.*;

/**
 * Represents a system which manage a conservation between different bots
 * each bot replies depending on the legality of the request he took
 * from previous bot.
 * each bot has a name and two arrays, one containing replies for legal requests
 * and the other for illegal requests.
 * reply is taken randomly from each array.
 * @author Elias Shubeita.
 */
class ChatterBot {

    /**
     * the prefix at start of the request that makes it legal.
     * it is constant.
     */
    static final String REQUEST_PREFIX = "say ";

    /**
     * the sub-string that is replaced by the Legal request
     * phrase (what comes after say) in the reply. it is constant.
     */
    static final String REQUESTED_PHRASE_PLACEHOLDER = "<phrase>";

    /**
     * the sub-string that is replaced by the Illegal request
     * phrase in the reply. it is constant.
     */
    static final String ILLEGAL_REQUEST_PLACEHOLDER = "<request>";

    /**
     * Random object that is used to get a random number.
     */
    Random rand = new Random();

    /**
     * array (field) that contains possible replies to Illegal requests.
     */
    String[] repliesToIllegalRequests;

    /**
     * array (field) that contains possible replies to Legal requests.
     */
    String[] repliesToLegalRequests;

    /**
     * field that contains the name of the current bot.
     */
    String name;

    /**
     * method that returns the name of the current bot.
     * @return name of the current bot.
     */
    String getName(){
        return this.name;
    }

    /**
     * Constructor.
     * @param name the name of the bot.
     * @param repliesToLegalRequest array of strings that contain possible replies to
     *                              legal requests.
     * @param repliesToIllegalRequest array of strings that contain possible replies to
     *                               illegal requests.
     */
    ChatterBot(String name, String[] repliesToLegalRequest, String[] repliesToIllegalRequest) {
        this.name = name;
        this.repliesToIllegalRequests = new String[repliesToIllegalRequest.length];
        this.repliesToLegalRequests = new String[repliesToLegalRequest.length];
        for(int i = 0; i < repliesToIllegalRequest.length; i++) {
            this.repliesToIllegalRequests[i] = repliesToIllegalRequest[i];
        }
        for(int i = 0; i < repliesToLegalRequest.length; i++) {
            this.repliesToLegalRequests[i] = repliesToLegalRequest[i];
        }
    }

    /**
     * method that takes a statement. if it begins with REQUEST_PREFIX,
     * the bot returns a random reply from repliesToLegalRequests array
     * replacing the placeholders in it by what comes after REQUEST_PREFIX.
     * else, it does similar thing but with repliesToIllegalRequests array.
     * @param statement the request that the bot takes to reply for.
     * @return the random reply described before (String).
     */
    String replyTo(String statement) {
        if(statement.startsWith(REQUEST_PREFIX)) {
            return respondToLegalRequest(statement);
        }
        return respondToIllegalRequest(statement);
    }

    /*
     * method takes a statement and calls the method replacePlaceholderInARandomPattern.
     */
    String respondToIllegalRequest(String statement){
        return replacePlaceholderInARandomPattern(repliesToIllegalRequests,
                ILLEGAL_REQUEST_PLACEHOLDER, statement);
    }

    /*
     * method that takes a statement with REQUEST_PREFIX. it removes
     * this prefix and call the method replacePlaceholderInARandomPattern.
     */
    String respondToLegalRequest(String statement)
    {
        String phrase = statement.replaceFirst(REQUEST_PREFIX, "");
        return replacePlaceholderInARandomPattern(repliesToLegalRequests,
                REQUESTED_PHRASE_PLACEHOLDER, phrase);
    }

    /*
     * this method gets a random index from zero to possibleReplies array length,
     * takes the value at index in possibleReplies array, replace all placeholders in
     * it to phraseToAdd and then return it.
     */
    String replacePlaceholderInARandomPattern(String[] possibleReplies, String placeholder,
                                            String phraseToAdd){
        int randomIndex = rand.nextInt(possibleReplies.length);
        String responsePattern = possibleReplies[randomIndex];
        //we don’t repeat the request prefix, so delete it from the reply.
        //phrase contains the words after prefix "say".
        return responsePattern.replaceAll(placeholder, phraseToAdd);
    }
}
