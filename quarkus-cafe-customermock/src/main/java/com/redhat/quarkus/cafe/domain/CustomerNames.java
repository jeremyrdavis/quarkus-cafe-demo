package com.redhat.quarkus.cafe.domain;

import java.util.*;
import java.util.stream.Collectors;

public class CustomerNames {

    static final List<String> names = Arrays.asList(
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
        "Chidambaram C");

    public static List<String> randomNames(int desiredNumberOfNames) {

        if (desiredNumberOfNames > names.size()) {
            throw new RuntimeException("try a smaller number");
        }

        return new Random()
                .ints(desiredNumberOfNames, 0, names.size())
                .mapToObj(i -> names.get(i))
                .collect(Collectors.toList());
    }


}
