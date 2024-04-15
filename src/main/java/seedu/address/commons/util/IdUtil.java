package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Random;

import jdk.jshell.spi.ExecutionControl;

/**
 * Generates unique  String IDs for patients, doctors, and appointments.
 *
 * At the moment the ID util is underutilised, but we have kept it in the code for future adaptation.
 * When the class was orignially defined, we envisioned it being essential to our use case, however, as development
 * progressed, we realised that it would not add much significant value at least until v1.4 of our product.
 * However, it can serve a purpose down the line, so we have left it in, despite it not being fleshed out completely.
 *
 * Currently only appointments are assigned an ID upon being created, but they ID itself does not serve a purpose.
 */
public class IdUtil {

    /**
     * Enum containing all possible entity types in our system.
     *
     * Associated characters are the first letter of each type of entity.
     */
    public enum Entities {
        PATIENT("p"),
        DOCTOR("d"),
        APPOINTMENT("a");

        private final String letter;
        Entities(String letter) {
            this.letter = letter;
        }

        /**
         * Returns letter associated with entity.
         * @return String letter
         */
        public String getLetter() {
            return letter;
        }

        /**
         * Gets entity object associated with character.
         *
         * @param c character in question
         * @return Entities entity object associated with input character
         */
        protected static Entities getEntityFromChar(char c) {
            if (c == 'a') {
                return Entities.APPOINTMENT;
            } else if (c == 'p') {
                return Entities.PATIENT;
            } else if (c == 'd') {
                return Entities.DOCTOR;
            }
            throw new IllegalArgumentException("Invalid character input - no corresponding entity");
        }
    }

    // EnumMap storing entities and their corresponding used up ids.
    private static final EnumMap<Entities, HashSet<String>> allIds = new EnumMap<>(Entities.class);

    /**
     * Generates a new id based on input entity.
     *
     * @param entity type of id to generate.
     * @return String id.
     */
    public static String generateNewId(Entities entity) {
        HashSet<String> idSet = allIds.get(entity);
        if (idSet == null) {
            idSet = new HashSet<>();
            allIds.put(entity, idSet);
        }

        Random random = new Random();
        String initId = String.valueOf(random.nextInt(90000000) + 10000000);
        while (idSet.contains(initId)) {
            initId = String.valueOf(random.nextInt(90000000) + 10000000);;
        }

        idSet.add(initId);
        assert initId.length() == 8 : "All numeric portions of IDs must be 8 digits long";

        return entity.getLetter() + initId;
    }

    /**
     * Deletes Id that is inputted.
     *
     * @param id String id to delete.
     */
    public static void deleteId(String id) {
        requireNonNull(id);
        char firstChar = id.substring(0, 1).charAt(0);
        assert firstChar == 'a' || firstChar == 'p' || firstChar == 'd' : "IDs can only start with these 3 letters";
        Entities entity = Entities.getEntityFromChar(firstChar);
        HashSet<String> idSet = allIds.get(entity);
        idSet.remove(id.substring(1, id.length()));
    }

    /**
     * Returns allIds as an unmodifiable map.
     *
     * @return unmodifiable map containing ids.
     */
    public static boolean hasId(String id) {
        requireNonNull(id);
        Entities entity = Entities.getEntityFromChar(id.substring(0, 1).charAt(0));
        HashSet<String> idSet = allIds.get(entity);
        return idSet.contains(id.substring(1, id.length()));
    }

    /**
     * Updates map with initial values from storage.
     * To be implemented in the future as it does not affect or impact current functionality.
     *
     * @throws ExecutionControl.NotImplementedException as it is not implemented yet and shouldn't be used.
     */
    public static void initalMapUpdate() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("to be implemented");
    }

}
