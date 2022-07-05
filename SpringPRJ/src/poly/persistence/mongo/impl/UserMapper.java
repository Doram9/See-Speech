package poly.persistence.mongo.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import poly.persistence.mongo.IUserMapper;
import poly.persistence.mongo.comm.AbstractMongoDBCommon;
import poly.util.CmmUtil;

import java.util.*;
import java.util.function.Consumer;

@Component("UserMapper")
public class UserMapper extends AbstractMongoDBCommon implements IUserMapper {

    private final MongoTemplate mongodb;

    private final Logger log = Logger.getLogger(this.getClass());

    @Autowired
    public UserMapper(MongoTemplate mongodb) {
        this.mongodb = mongodb;
    }

    @Override
    public void insertUser(String colNm, Map<String, Object> pMap) throws Exception {

        log.info(this.getClass().getName() + ".insertUser Start!");

        // 컬렉션이 없다면 컬렉션 생성
        super.createCollection(colNm, "user_email");

        MongoCollection<Document> collection = mongodb.getCollection(colNm);

        collection.insertOne(new Document(pMap));

        log.info(this.getClass().getName() + ".insertUser End!");

    }

    @Override
    public Map<String, String> getUserExistForAJAX(String colNm, String user_email) throws Exception {

        log.info(this.getClass().getName() + ".getUserExistForAJAX Start!");

        Map<String, String> rMap = new HashMap<>();

        MongoCollection<Document> collection = mongodb.getCollection(colNm);

        // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

        Document query = new Document();

        query.append("user_email", user_email);

        Consumer<Document> processBlock = document -> rMap.put("res", "exist");

        collection.find(query).forEach(processBlock);

        log.info(this.getClass().getName() + ".getUserExistForAJAX End!");

        return rMap;
    }

    @Override
    public Map<String, String> getUser(String colNm, Map<String, String> pMap) throws Exception {

        log.info(this.getClass().getName() + ".getUser start");

        Map<String, String> rMap = new HashMap<>();

        MongoCollection<Document> collection = mongodb.getCollection(colNm);

        // Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

        Document query = new Document();

        query.append("user_email", pMap.get("user_email"));
        query.append("user_pw", pMap.get("user_pw"));

        Consumer<Document> processBlock = document -> {
            String user_email = document.getString("user_email");
            String user_name = document.getString("user_name");

            rMap.put("user_email", user_email);
            rMap.put("user_name", user_name);
        };

        collection.find(query).forEach(processBlock);

        log.info(this.getClass().getName() + ".getUser end");

        return rMap;
    }

    @Override
    public int updateUserPw(String colNm, Map<String, Object> pMap) throws Exception {

        log.info(this.getClass().getName() + ".updateUserPwForFind start");

        MongoCollection<Document> collection = mongodb.getCollection(colNm);

        Document findQuery = new Document();
        findQuery.append("user_email", pMap.get("user_email"));
        findQuery.append("user_name", pMap.get("user_name"));

        Document updateQuery = new Document();
        updateQuery.append("user_pw", pMap.get("user_pw"));

        UpdateResult updateResults = collection.updateOne(findQuery, new Document("$set", updateQuery));
        int res = (int) updateResults.getMatchedCount();
        log.info("res : " + res);

        log.info(this.getClass().getName() + ".updateUserPwForFind end");

        return res;
    }

    @Override
    public int deleteUser(String colNm, Map<String, Object> pMap) throws Exception {

        log.info(this.getClass().getName() + ".updateUserPwForFind start");

        MongoCollection<Document> collection = mongodb.getCollection(colNm);

        DeleteResult deleteResult = collection.deleteOne(new Document(pMap));
        int res = (int) deleteResult.getDeletedCount();

        log.info(this.getClass().getName() + ".updateUserPwForFind end");

        return res;
    }

    @Override
    public int saveUserRate(String colNm, Map<String, Object> pMap) throws Exception {

        log.info(this.getClass().getName() + ".saveUserRate start");

        MongoCollection<Document> collection = mongodb.getCollection(colNm);

        Document findQuery = new Document();
        findQuery.append("user_email", pMap.get("user_email"));
        findQuery.append("user_name", pMap.get("user_name"));

        Document updateQuery = new Document();
        updateQuery.append("user_tmpCNT", pMap.get("user_tmpCNT"));
        updateQuery.append("user_wrongCNT", pMap.get("user_wrongCNT"));
        updateQuery.append("user_rate", pMap.get("user_rate"));

        //Math.round((TMP_CNT - WRONG_CNT) / TMP_CNT * 100),

        UpdateResult updateResults = collection.updateOne(findQuery, new Document("$set", updateQuery));
        int res = (int) updateResults.getMatchedCount();
        log.info("res : " + res);

        log.info(this.getClass().getName() + ".saveUserRate end");

        return res;
    }

    @Override
    public List<Map<String, String>> getUserList(String colNm) throws Exception {

        log.info(this.getClass().getName() + ".getUserList start");

        List<Map<String, String>> rList = new ArrayList<>();

        MongoCollection<Document> collection = mongodb.getCollection(colNm);

        Document query = new Document();

        Document projection = new Document();

        projection.append("user_name", "$user_name");
        projection.append("user_rate", "$user_rate");
        projection.append("_id", 0);

        Document sort = new Document();

        sort.append("user_rate", -1);

        Consumer<Document> processBlock = document -> {

            String user_name = CmmUtil.nvl(document.getString("user_name"));
            String user_rate = String.valueOf(document.getInteger("user_rate", 0));

            Map<String, String> rMap = new LinkedHashMap<>();

            rMap.put("user_name", user_name);
            rMap.put("user_rate", user_rate);

            // 레코드 결과를 List에 저장하기
            rList.add(rMap);
        };

        collection.find(query).projection(projection).sort(sort).forEach(processBlock);

        log.info(this.getClass().getName() + ".getUserList end");

        return rList;
    }

}
