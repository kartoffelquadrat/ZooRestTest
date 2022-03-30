package eu.kartoffelquadrat.zooresttest;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import eu.kartoffelquadrat.zoo.Animal;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

/**
 * All Unit tests for Assortment Resources
 */
public class ZooTest
        extends RestTestUtils {

    /**
     * Verify that GET on /isbns returns 200 and expected catalogue. Every resource is covered by exactly one test.
     */
    @Test
    public void testOpeningHoursGet() throws UnirestException {

        // Try to retrieve catalogue
        HttpResponse<String> openingHours = Unirest.get(getServiceURL("/openinghours")).asString();
        verifyOk(openingHours);

        // Verify and return catalogue content
        String body = openingHours.getBody();
        assert body.contains("9AM");

    }

    /**
     * Verify that GET on /isbns/{isbn} returns 200 and expected book details
     */
    @Test
    public void testAllAnimalsGet() throws UnirestException {

        // Try to retrieve catalogue
        HttpResponse<String> allAnimals = Unirest.get(getServiceURL("/animals")).asString();
        verifyOk(allAnimals);

        // Deserialize result
        Collection<Animal> animals = new Gson().fromJson(allAnimals.getBody(), new LinkedList<Animal>().getClass());

        // Verify catalogue content
        assert animals.size() == 3;
    }

    /**
     * Verify one of the built in animals exists.
     */
    @Test
    public void testAnimalsAnimalGet() throws UnirestException {

        assert isExistent("Nemo");
    }

    /**
     * Verify that PUT on /isbns/{isbn} returns 200 and allows adding a book to catalogue. Also verifies the new isbn
     * appears in list and subsequently removes it, to leave server is original state.
     */
    @Test
    public void testAnimalsAnimalPut() throws UnirestException {

        // Generate random animal name
        String name = getRandomString();
        Animal animal = new Animal("Chimpanzee", 10, "Bananas");
        String animalJsonString = new Gson().toJson(animal);

        // Verify catalogue content (must now contain the new book)
        HttpResponse addAnimalReply = Unirest.put(getServiceURL("/animals/" + name)).header("Content-Type", "application/json").body(animalJsonString).asString();
        verifyOk(addAnimalReply);

        // Verify the animal exists
        assert isExistent(name);
    }

    private boolean isExistent(String animalName) {

        // Try to retrieve catalogue
        try {
            HttpResponse<String> animalDetails = Unirest.get(getServiceURL("/animals/" + animalName)).asString();
            verifyOk(animalDetails);

            // Verify catalogue content, try to Parse to Animal object
            Animal animal = new Gson().fromJson(animalDetails.getBody(), Animal.class);

            boolean ageOk = animal.getAge() >= 0;
            boolean foodOk = !animal.getFavouriteFood().isEmpty();
            boolean speciesOk = !animal.getSpecies().isEmpty();

            // Api result is ok if all fields were correctly parsed.
            return ageOk && foodOk && speciesOk;
        } catch (UnirestException unirestException) {

            // Interpret API error as "animal does not exist"
            return false;
        }
    }

    /**
     * Random Alphabet String generator: https://stackoverflow.com/a/20536597
     *
     * @return random A-Z String, length 10
     */
    protected String getRandomString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
}
