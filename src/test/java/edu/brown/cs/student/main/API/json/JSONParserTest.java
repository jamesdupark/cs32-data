package edu.brown.cs.student.main.API.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.google.gson.JsonSyntaxException;
import edu.brown.cs.student.main.Recommender.Stud.StudentInfo;
import edu.brown.cs.student.main.Recommender.Stud.StudentMatch;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class JSONParserTest {
  String jsonMessage, jsonStudentInfo, jsonStudentMatch, jsonActiveList,
      jsonStudentInfoList, jsonStudentMatchList;
  Message message;
  StudentInfo info;
  StudentMatch match;
  List<String> activeList;
  List<StudentInfo> infos;
  List<StudentMatch> matches;

  @Before
  public void init() {
    // initialize json objects
    jsonMessage = "{\"message\": \"hello world!\"}";
    jsonStudentInfo = "{\"id\": 1, \"name\": \"Petr Dillingstone\", "
        + "\"class_year\": \"sophomore\", \"years_experience\": 8, \"communication_style\": "
        + "\"email\", \"weekly_avail_hours\": 13, \"meeting_style\": \"in person\", "
        + "\"meeting_time\": \"afternoon\"}";
    jsonStudentMatch = "{\"id\": 1, \"name\": \"Petr Dillingstone\", \"gender\": \"Female\", "
        + "\"ssn\": \"281-66-4039\", \"nationality\": \"Egypt\", \"race\": "
        + "\"and Native Hawaiian or Other Pacific Islander\", \"software_engn_confidence\": 41}";
    jsonActiveList = "[\"/match-one\", \"/match-two\", \"/match-three\"]";
    jsonStudentInfoList = "[" + jsonStudentInfo + ", {\"id\": 2, \"name\": \"Gerri Enterle\", "
        + "\"class_year\": \"sophomore\", \"years_experience\": 19, \"communication_style\": "
        + "\"zoom\", \"weekly_avail_hours\": 19, \"meeting_style\": \"in person\", "
        + "\"meeting_time\": \"morning\"}]";
    jsonStudentMatchList = "[" + jsonStudentMatch + ", {\"id\": 2, \"name\": \"Gerri Enterle\", "
        + "\"gender\": \"Female\", \"ssn\": \"193-55-9443\", \"nationality\": \"Saudi Arabia\", "
        + "\"race\": \"Black or African American\", \"software_engn_confidence\": 76}]";
    // initialize corresponding objects after parsing
    message = new Message("hello world!");
    info = new StudentInfo(List.of(1, 8, 13),
        List.of("Petr Dillingstone", "sophomore", "email", "in person", "afternoon"));
    match = new StudentMatch(1, 41, "Petr Dillingstone",
        "Female", "Egypt", "and Native Hawaiian or Other Pacific Islander");
    // intialize json lists
    activeList = List.of("/match-one", "/match-two", "/match-three");
    StudentInfo infoTwo = new StudentInfo(List.of(2, 19, 19),
        List.of("Gerri Enterle", "sophomore", "zoom", "in person", "morning"));
    StudentMatch matchTwo = new StudentMatch(2, 76, "Gerri Enterle",
        "Female", "Saudi Arabia", "Black or African American");
    infos = List.of(info, infoTwo);
    matches = List.of(match, matchTwo);
  }


  @Test
  public void testGetMessage() {
    assertEquals(message.getMessage(), JSONParser.getMessage(jsonMessage));
    assertThrows(JsonSyntaxException.class, () -> JSONParser.getMessage("[yes: ]"));
  }

  @Test
  public void testToStringList() {
    assertEquals(activeList, JSONParser.toStringList(jsonActiveList));
    assertThrows(JsonSyntaxException.class, () -> JSONParser.toStringList("[yes: ]"));
  }

  @Test
  public void testGetJsonObject() {
    assertEquals(info, JSONParser.getJsonObject(jsonStudentInfo, StudentInfo.class));
    assertEquals(match, JSONParser.getJsonObject(jsonStudentMatch, StudentMatch.class));
    assertThrows(JsonSyntaxException.class,
        () -> JSONParser.getJsonObject("asdf", Message.class));
    assertThrows(JsonSyntaxException.class,
        () -> JSONParser.getJsonObject("[]", StudentMatch.class));
  }

  @Test
  public void testGetJsonObjectList() {
    List<StudentInfo> testList = JSONParser.getJsonObjectList(jsonStudentInfoList, StudentInfo.class);
    List<StudentMatch> testList2 = JSONParser.getJsonObjectList(jsonStudentInfoList, StudentMatch.class);
    List<Message> testList3 = JSONParser.getJsonObjectList(jsonStudentInfoList, Message.class);
    assertEquals(infos, JSONParser.getJsonObjectList(jsonStudentInfoList, StudentInfo.class));
    assertEquals(matches, JSONParser.getJsonObjectList(jsonStudentMatchList, StudentMatch.class));
    assertThrows(JsonSyntaxException.class,
        () -> JSONParser.getJsonObjectList("[yes]", Message.class));
  }

  @Test
  public void testReadJsonFile() throws IOException {
    String infoFilePath = "data/recommendation/json/studentInfoTest.json";
    String matchFilePath = "data/recommendation/json/studentMatchTest.json";
    String csvFilePath = "data/stars/ten-star.csv";
    assertEquals(infos, JSONParser.readJsonFile(infoFilePath, StudentInfo.class));
    assertEquals(matches, JSONParser.readJsonFile(matchFilePath, StudentMatch.class));
    assertThrows(IOException.class, () -> JSONParser.readJsonFile("", StudentInfo.class));
    assertThrows(IOException.class, () -> JSONParser.readJsonFile(csvFilePath, StudentInfo.class));
  }
}
