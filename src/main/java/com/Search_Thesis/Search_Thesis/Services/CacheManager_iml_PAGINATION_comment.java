package com.Search_Thesis.Search_Thesis.Services;

import com.Search_Thesis.Search_Thesis.Model.Comment_Reply_Question;
import com.Search_Thesis.Search_Thesis.Model.Question;
import com.Search_Thesis.Search_Thesis.Model.Reply;
import com.Search_Thesis.Search_Thesis.resposity.Question_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.jcache.JCacheCache;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.cache.Cache;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Scope("prototype")
@Service
public class CacheManager_iml_PAGINATION_comment implements  Cache_Manager<String> {


    @Autowired
    Reply reply;

    @Autowired
    CacheManager cacheManager;

    @Autowired
    Question_Repository question_repository;


    @Override
    public String getCache(String id, String... value) {
        return null;
    }

    public boolean Update_cache_Reply(String id, String reply_id, Comment_Reply_Question comment_reply_question, List<Reply> reply_List) {
        String cache_name = "reply_page";
        String key = "";

        Question question1 = question_repository.findByQuestion_id(Integer.parseInt(id));

        List<Reply> replyList = (List<Reply>) question1.getReply();
        Collections.reverse(replyList);

        List<List<Integer>> page_list = new ArrayList<>();

        int total_reply = replyList.size();

        int page = total_reply / 10;

        int compoment_in_last_page = total_reply - page * 10;

        System.out.println(total_reply + "," + page);

        for (int i = 0; i < page; i++) {
            int start_id = replyList.get(i * 10).getReply_id();

            int end_id = 0;
            try {
                end_id = replyList.get(i * 10 + 8).getReply_id();
            } catch (Exception e) {
                end_id = replyList.get(replyList.size() - 1).getReply_id();
            }

            List<Integer> element = new ArrayList<>();
            element.add(start_id);
            element.add(end_id);

            page_list.add(element);

        }

        List<Integer> element = new ArrayList<>();
        element.add(replyList.get(page * 10 - 1).getReply_id());
        element.add(replyList.get(replyList.size() - 1).getReply_id());

        page_list.add(element);


        for (int i = 0; i < page_list.size(); i++) {
            System.out.println(page_list.get(i).get(0));
            System.out.println(page_list.get(i).get(1));
            if (Integer.valueOf(reply_id) <= page_list.get(i).get(0) && Integer.valueOf(reply_id) >= page_list.get(i).get(1)) {
                key = id + "-" + String.valueOf(i + 1);
                break;
            }
        }
        System.out.println(key);

        List<Reply> replyList1 = (List<Reply>) cacheManager.getCache("reply_page").get(key).get();
        List<Reply> replyList2 = new ArrayList<>();

        List<Reply> type = reply_List;
        Reply reply1 = new Reply();
        int pos = 0;


        for (int i = 0; i < replyList1.size(); i++) {
            Reply re = replyList1.get(i);
            replyList2.add(re);
            if (re.getReply_id() == Integer.valueOf(reply_id)) {
                System.out.println("test");

                reply1 = org.apache.commons.lang3.SerializationUtils.clone(re);
                pos = i;

                reply1.getComment_reply_questionList().add(comment_reply_question);

            }


//         reply1.getComment_reply_questionList().add(comment_reply_question);
//         replyList1.ad//         cacheManager.getCache(cache_name).put(key ,  replyList1);d(reply1) ;
        }
        try {

            replyList2.remove(pos);

            replyList2.add(pos, reply1);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        cacheManager.getCache(cache_name).evict(key);
        cacheManager.getCache(cache_name).put(key, replyList2);

        return false;
    }

    @Cacheable(value = "reply_page", key = "#id.concat('-').concat( #page_num)")
    public List<Reply> get_reply_list_from_Cache(String id, String page_num, List<Reply> list) {
        javax.cache.Cache<Object, Object> jcache = (Cache<Object, Object>) cacheManager.getCache("reply_page").get(id + "-" + page_num);
        if (jcache != null) {
            System.out.println(new JCacheCache(jcache));
        }
        return list;

//
    }

    @Override
    public boolean Update_Cache(int page_size, String t, String... cache_key) {
//        try {
//
//            String Cache_key = Arrays.stream(cache_key).toList().get(0);
//
//            List<Object> list = Collections.singletonList(question_repository.findAll());
//
//            PagedListHolder pagedListHolder = new PagedListHolder<>(list);
//
//            pagedListHolder.setPageSize(page_size);
//
//            int total_page = pagedListHolder.getPageCount();
//            if (cache_key == null) {
//
//                cacheManager.getCache(name_cache).clear();
//
//            }
//
//            for (int i = 1; i <= total_page; i++) {
//                pagedListHolder.setPageSize(page_size);
//                pagedListHolder.setPage(i);
//                String key =   Cache_key +  "-" +  String.valueOf(i) ;
//                System.out.println(key);
//
//                if (Cache_key != null) {
//
//                    cacheManager.getCache(name_cache).evict(key);
//
//                }
//
////                cacheManager.getCache(name_cache).put(key, lst.stream().toList());
//            }
//
//            return true;
//        }
//        catch (Exception e) {
//            System.out.println(e.getMessage());
        return false;

    }

}
