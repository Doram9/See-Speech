package poly.persistence.mongo.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import poly.persistence.mongo.IQuizMapper;
import poly.persistence.mongo.comm.AbstractMongoDBCommon;
import poly.util.CmmUtil;
import poly.util.EncryptUtil;

import java.util.*;
import java.util.function.Consumer;

@Component("QuizMapper")
public class QuizMapper extends AbstractMongoDBCommon implements IQuizMapper {

    private final MongoTemplate mongodb;

    private final Logger log = Logger.getLogger(this.getClass());

    @Autowired
    public QuizMapper(MongoTemplate mongodb) {
        this.mongodb = mongodb;
    }

    @Override
    public List<Map<String, String>> getQuizList(String colNm) throws Exception {

        log.info(this.getClass().getName() + ".getQuizList Start!");

        List<Map<String, String>> rQuizList = new ArrayList<>();

        MongoCollection<Document> collection = mongodb.getCollection(colNm);

        // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

        Document query = new Document();

        Consumer<Document> processBlock = document -> {

            String quizTitle = CmmUtil.nvl(document.getString("quiz_title"));
            String quizSort = CmmUtil.nvl(document.getString("quiz_sort"));

            Map<String, String> rMap = new LinkedHashMap<>();

            rMap.put("quiz_title", quizTitle);
            rMap.put("quiz_sort", quizSort);

            // 레코드 결과를 List에 저장하기
            rQuizList.add(rMap);

        };

        collection.find(query).forEach(processBlock);

        log.info(this.getClass().getName() + ".getQuizList end!");

        return rQuizList;
    }

    @Override
    public List<String> getQuizContList(String colNm, String quizTitle, String quizSort) throws Exception {

        log.info(this.getClass().getName() + ".getQuizContList Start!");

        List<String> rQuizContList = new ArrayList<>();

        MongoCollection<Document> collection = mongodb.getCollection(colNm);

        // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

        Document query = new Document();

        query.append("quiz_title", quizTitle);
        query.append("quiz_sort", quizSort);

        Document projection = new Document();

        projection.append("quiz_cont", "$quiz_cont");
        projection.append("_id", 0);

        Consumer<Document> processBlock = document -> {
            List<String> pQuizContList = document.getList("quiz_cont", String.class);
            rQuizContList.addAll(pQuizContList);
        };

        collection.find(query).projection(projection).forEach(processBlock);

        log.info(this.getClass().getName() + ".getQuizContList end!");

        return rQuizContList;
    }

    @Override
    public void insertUserQuiz(String userQuizColNm, Map<String, String> pMap) throws Exception {

        log.info(this.getClass().getName() + ".insertQuiz start!");

        String user_email = EncryptUtil.encAES128CBC(pMap.get("user_email"));
        String quiz_title = pMap.get("quiz_title");
        String strQuizList = pMap.get("strQuizList");

        List<String> quiz_cont = Arrays.asList(strQuizList.split(","));

        log.info(this.getClass().getName() + ".user_email : " + user_email);
        log.info(this.getClass().getName() + ".quiz_title : " + quiz_title);
        log.info(this.getClass().getName() + ".strQuizList : " + strQuizList);
        log.info(this.getClass().getName() + ".quiz_cont : " + quiz_cont);

        Map<String, Object> rMap = new LinkedHashMap<>();
        rMap.put("user_email", user_email);
        rMap.put("quiz_title", quiz_title);
        rMap.put("quiz_cont", quiz_cont);

        // 컬렉션이 없다면 컬렉션 생성
        super.createCollection(userQuizColNm, "user_email");

        MongoCollection<Document> collection = mongodb.getCollection(userQuizColNm);

        collection.insertOne(new Document(rMap));

        log.info(this.getClass().getName() + ".insertQuiz end!");

    }

    @Override
    public List<String> getUserQuizTitleList(String userQuizColNm, String user_email) throws Exception {

        log.info(this.getClass().getName() + ".getUserQuizTitleList start!");

        List<String> rUserQuizTitleList = new ArrayList<>();

        MongoCollection<Document> collection = mongodb.getCollection(userQuizColNm);

        // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

        Document query = new Document();
        query.append("user_email", user_email);

        Document projection = new Document();
        projection.append("quiz_title", "$quiz_title");
        projection.append("_id", 0);

        Consumer<Document> processBlock = document -> rUserQuizTitleList.add(document.getString("quiz_title"));

        collection.find(query).projection(projection).forEach(processBlock);

        log.info(this.getClass().getName() + ".getUserQuizTitleList end!");

        return rUserQuizTitleList;
    }

    @Override
    public List<String> getUserQuizContList(String userQuizColNm, String user_email, String quizTitle) throws Exception {

        log.info(this.getClass().getName() + ".getUserQuizContList start!");

        List<String> rUserQuizContList = new ArrayList<>();

        MongoCollection<Document> collection = mongodb.getCollection(userQuizColNm);

        // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

        Document query = new Document();
        query.append("quiz_title", quizTitle);
        query.append("user_email", user_email);

        Document projection = new Document();
        projection.append("quiz_cont", "$quiz_cont");
        projection.append("_id", 0);

        Consumer<Document> processBlock = document -> {
            List<String> pUserQuizContList = document.getList("quiz_cont", String.class);
            rUserQuizContList.addAll(pUserQuizContList);
        };

        collection.find(query).projection(projection).forEach(processBlock);

        log.info(this.getClass().getName() + ".getUserQuizContList end!");

        return rUserQuizContList;
    }

    @Override
    public boolean isQuizTitleExistForAJAX(String userQuizColNm, String user_email, String quiz_title) throws Exception {

        log.info(this.getClass().getName() + ".isQuizTitleExistForAJAX start!");

        List<String> rUserQuizList = new ArrayList<>();

        MongoCollection<Document> collection = mongodb.getCollection(userQuizColNm);

        // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

        Document query = new Document();
        query.append("user_email", user_email);
        query.append("quiz_title", quiz_title);

        Consumer<Document> processBlock = document -> rUserQuizList.add(document.getString("quiz_title"));

        collection.find(query).forEach(processBlock);

        log.info(this.getClass().getName() + ".isQuizTitleExistForAJAX end!");

        if (rUserQuizList.isEmpty()) {
            return false;
        } else {
            return true;
        }

    }

    @Override
    public int deleteUserQuiz(String userQuizColNm, Map<String, Object> pMap) throws Exception {

        log.info(this.getClass().getName() + ".deleteUserQuiz start!");

        Map<String, Object> rMap = new HashMap<>();
        rMap.put("user_email", pMap.get("user_email"));

        MongoCollection<Document> collection = mongodb.getCollection(userQuizColNm);

        DeleteResult deleteResult = collection.deleteMany(new Document(rMap));
        int res = (int) deleteResult.getDeletedCount();

        log.info(this.getClass().getName() + ".deleteUserQuiz end!");

        return res;
    }

    @Override
    public int deleteOneQuiz(String userQuizColNm, Map<String, Object> pMap) throws Exception {

        log.info(this.getClass().getName() + ".deleteOneQuiz start!");

        MongoCollection<Document> collection = mongodb.getCollection(userQuizColNm);

        DeleteResult deleteResult = collection.deleteMany(new Document(pMap));
        int res = (int) deleteResult.getDeletedCount();

        log.info(this.getClass().getName() + ".deleteOneQuiz end!");

        return res;
    }

    @Override
    public int updateUserQuiz(String userQuizColNm, Map<String, String> pMap) throws Exception {

        log.info(this.getClass().getName() + ".updateUserQuiz start!");

        String user_email = EncryptUtil.encAES128CBC(pMap.get("user_email"));
        String quiz_title = pMap.get("quiz_title");
        String strQuizList = pMap.get("strQuizList");

        List<String> quiz_cont = Arrays.asList(strQuizList.split(","));

        log.info(this.getClass().getName() + ".user_email : " + user_email);
        log.info(this.getClass().getName() + ".quiz_title : " + quiz_title);
        log.info(this.getClass().getName() + ".strQuizList : " + strQuizList);
        log.info(this.getClass().getName() + ".quiz_cont : " + quiz_cont);

        MongoCollection<Document> collection = mongodb.getCollection(userQuizColNm);

        Document findQuery = new Document();
        findQuery.append("user_email", user_email);
        findQuery.append("quiz_title", quiz_title);

        Document updateQuery = new Document();
        updateQuery.append("quiz_cont", quiz_cont);

        UpdateResult updateResults = collection.updateOne(findQuery, new Document("$set", updateQuery));
        int res = (int) updateResults.getMatchedCount();
        log.info("res : " + res);
        log.info("updateResults : " + updateResults);

        log.info(this.getClass().getName() + ".updateUserQuiz end!");

        return res;
    }

    @Override
    public void insertAdminQuiz(String colNm, Map<String, String> pMap) throws Exception {

        log.info(this.getClass().getName() + ".insertAdminQuiz start!");

        String quiz_sort = pMap.get("quiz_sort");
        String quiz_title = pMap.get("quiz_title");
        String strQuizList = pMap.get("strQuizList");

        List<String> quiz_cont = Arrays.asList(strQuizList.split(","));

        log.info(this.getClass().getName() + ".quiz_sort : " + quiz_sort);
        log.info(this.getClass().getName() + ".quiz_title : " + quiz_title);
        log.info(this.getClass().getName() + ".strQuizList : " + strQuizList);
        log.info(this.getClass().getName() + ".quiz_cont : " + quiz_cont);

        Map<String, Object> rMap = new LinkedHashMap<>();
        rMap.put("quiz_sort", quiz_sort);
        rMap.put("quiz_title", quiz_title);
        rMap.put("quiz_cont", quiz_cont);

        // 컬렉션이 없다면 컬렉션 생성
        super.createCollection(colNm, "quiz_title");

        MongoCollection<Document> collection = mongodb.getCollection(colNm);

        collection.insertOne(new Document(rMap));

        log.info(this.getClass().getName() + ".insertAdminQuiz end!");

    }

    @Override
    public int deleteOneAdminQuiz(String colNm, Map<String, Object> pMap) throws Exception {

        log.info(this.getClass().getName() + ".deleteOneAdminQuiz start!");

        MongoCollection<Document> collection = mongodb.getCollection(colNm);

        DeleteResult deleteResult = collection.deleteMany(new Document(pMap));
        int res = (int) deleteResult.getDeletedCount();

        log.info(this.getClass().getName() + ".deleteOneAdminQuiz end!");

        return res;
    }

    @Override
    public int updateAdminQuiz(String colNm, Map<String, String> pMap) throws Exception {

        log.info(this.getClass().getName() + ".updateAdminQuiz start!");

        String quiz_title = pMap.get("quiz_title");
        String strQuizList = pMap.get("strQuizList");

        List<String> quiz_cont = Arrays.asList(strQuizList.split(","));

        log.info(this.getClass().getName() + ".quiz_title : " + quiz_title);
        log.info(this.getClass().getName() + ".strQuizList : " + strQuizList);
        log.info(this.getClass().getName() + ".quiz_cont : " + quiz_cont);

        MongoCollection<Document> collection = mongodb.getCollection(colNm);

        Document findQuery = new Document();
        findQuery.append("quiz_title", quiz_title);

        Document updateQuery = new Document();
        updateQuery.append("quiz_cont", quiz_cont);

        UpdateResult updateResults = collection.updateOne(findQuery, new Document("$set", updateQuery));
        int res = (int) updateResults.getMatchedCount();
        log.info("res : " + res);
        log.info("updateResults : " + updateResults);

        log.info(this.getClass().getName() + ".updateAdminQuiz end!");

        return res;
    }
}
