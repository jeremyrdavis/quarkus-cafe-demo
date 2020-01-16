package com.redhat.quarkus.cafe.domain;

import java.util.*;
import java.util.stream.Collectors;

public class CustomerNames {

    static final List<String> names = Arrays.asList(
            "Chidambaram C",
            "Larsson L",
            "premkumar p",
            "Jaganaathan J",
            "Fokou F",
            "Paul P",
            "Regas R",
            "doshi d",
            "Maddali M",
            "Sangem S",
            "Sangem S",
            "kunta k",
            "Mateti M",
            "Bergh B",
            "Sinha S",
            "Sahu S",
            "bhatt b",
            "Birkenberger B",
            "Burrell B",
            "Calingasan C",
            "Chai C",
            "Durning D",
            "Gaitan G",
            "Hortelano H",
            "Khanna K",
            "Latif L",
            "Neece N",
            "Nunez N",
            "Patan P",
            "Tekle T",
            "Tucker T",
            "Wang W",
            "Esclamado E",
            "Jennings J",
            "Gomez G",
            "Seth S",
            "Babaoglu B",
            "Kudumuri K",
            "Brown B",
            "NEERUKONDA N",
            "Patil P",
            "Randell R",
            "Agnihotri A",
            "Lamtahri L",
            "Sandlin S",
            "Norman N",
            "addison a",
            "DesPres D",
            "Morris M",
            "iStefan L",
            "Prasanth p",
            "Raghu J",
            "Etienne F",
            "Avik P",
            "Valarie R",
            "smita d",
            "Ram M",
            "Omkaram S",
            "Omkaram S",
            "vivek k",
            "Vijay M",
            "Chip B",
            "Prasoon S",
            "Indrajeet S",
            "nirmal b",
            "Lisa B",
            "Kelvina B",
            "Carlo C",
            "Mario C",
            "Marilyn D",
            "Freddy G",
            "Arvin H",
            "Rajesh K",
            "Alex L",
            "Ned N",
            "Jose N",
            "Jilani P",
            "Henok T",
            "Hilda T",
            "Steve W",
            "Edgar E",
            "Michele J",
            "Anthony G",
            "Gaurav S",
            "Seza B",
            "Partha K",
            "Steve B",
            "VEERA N",
            "Shivraj P",
            "Kelvin R",
            "Sachin A",
            "Mohammed L",
            "Chris S",
            "David N",
            "martin a",
            "Lawrence D",
            "Drewry M",
            "Venugopal C");

    public static List<String> randomNames(int desiredNumberOfNames) {

        if (desiredNumberOfNames > names.size()) {
            throw new RuntimeException("try a smaller number");
        }

        return new Random()
                .ints(desiredNumberOfNames, 0, names.size())
                .mapToObj(i -> names.get(i))
                .collect(Collectors.toList());
    }

    public static String randomName() {

        Collections.shuffle(names);
        Random rand = new Random();
        return names.get(rand.nextInt(names.size()));
    }


}
