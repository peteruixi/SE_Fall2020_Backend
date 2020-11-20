package com.healthMonitor.fall2020.orm;


import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-8-18
 * Time: 下午3:04
 * To change this template use File | Settings | File Templates.
 */
public interface IDataDao {
	
	void setBackupDao(IDataDao dao);
	
    <T > int add(T entity);
    <T > int batchAdd(Collection<T> entityCollection);
    <T , PK extends Serializable> int delete(PK id, Class<T> entityClass);

    <T , PK extends Serializable> int batchDelete(final PK[] ids, Class<T> entityClass);
    <T > int delete(Class<T> entityClass, String where, Object... args);
    <T > int delete(Class<T> entityClass, String where, Map<String, Object> params);
    <T > int update(T entity, Class<T> entityClass);
    <T > int update(T entity);
    <T > int batchUpdate(Collection<T> entityCollection, Class<T> entityClass);
    //若实体类有使用javax.persistence.Transient注解修饰的属性，则更新实体类时要用updateNew方法，不能用update方法，否则将报错
    <T > int updateNew(T entity);
    <T > int batchUpdateNew(Collection<T> entityCollection, Class<T> entityClass);
    <T , PK extends Serializable> T get(PK id, Class<T> entityClass);
    <T > T get(Class<T> entityClass, String where, Object... args);
    <T > T get(Class<T> entityClass, String where, Map<String, Object> params);
    <T > long queryTotalRows(Class<T> entityClass, String where, Object... args);
    <T > long queryTotalRows(Class<T> entityClass, String where, Map<String, Object> params);
    long getCount(String sql, Object... args);
    long getCount(String sql, Map<String, Object> params);

    String uniqueResult(String sql, Object... args) ;
    String uniqueResult(String sql, Map<String, Object> params) ;
    <T > List<T> query(Class<T> entityClass);
    <T > List<T> query(Class<T> entityClass, String where, Object... args);
    <T > List<T> query(Class<T> entityClass, String where, Map<String, Object> params);
    <T > boolean check(Class<T> entityClass, String key, Object value);
    <T> T getBySql(String sql, Class entityClass, Object... args);
    <T > T getBySql(String sql, RowMapper<T> rowMapper, Object... args);
    <T > T getBySql(String sql, RowMapper<T> rowMapper, Map<String, Object> params);
    <T > List<T> queryBySql(String sql, RowMapper<T> rowMapper, Object... args);
    <T > List<T> queryBySql(String sql, Class entityClass, Object... args);
    <T > List<T> queryBySql(String sql, RowMapper<T> rowMapper, Map<String, Object> params);
    <T > List<T> queryBySql(String sql, Class entityClass, Map<String, Object> params);
    <T > int truncate(Class<T> entityClass);
    void executeSql(String sql);
    int executeSql(String sql, Object... args);
    int executeSql(String sql, Map<String, Object> params);
    Map<String, Object> queryBySql(String sql, Object... args);
    Map<String, Object> queryBySql(String sql, Map<String, Object> params);
    List<Map<String, Object>> queryMap(String sql, Object... args);
    List<Map<String, Object>> queryMap(String sql, Map<String, Object> params);
    <T > Page queryList(Page page, String sql, Class<T> entityClass, Object... args);
    <T > Page queryList(Page page, String sql, Class<T> entityClass, Map<String, Object> params);
    Page queryMap(Page page, String sql, Object... args);
    Page queryMap(String sql, Page page, Map<String, Object> params);
    <T> Page queryListForUpdate(Page page, String sql, Class<T> entityClass,
                                Object[] args);
    <T>Page queryListForUpdate(Page page, String sql, Class<T> entityClass,
                               Map<String, Object> params);
    Page queryMapForUpdate(Page page, String sql, Object[] args);
    Page queryMapForUpdate(String sql, Page page, Map<String, Object> params);
    
    
    long getCountFast(String sql, Object... args);
    long getCountFast(String sql, Map<String, Object> params);
    <T > Page queryListFast(Page page, String sql, Class<T> entityClass, Object... args);
    <T > Page queryListFast(Page page, String sql, Class<T> entityClass, Map<String, Object> params);
    Page queryMapFast(Page page, String sql, Object... args);
    Page queryMapFast(String sql, Page page, Map<String, Object> params);
    <T> Page queryListForUpdateFast(Page page, String sql, Class<T> entityClass,
                                    Object[] args);
    <T>Page queryListForUpdateFast(Page page, String sql, Class<T> entityClass,
                                   Map<String, Object> params);
    Page queryMapForUpdateFast(Page page, String sql, Object[] args);
    Page queryMapForUpdateFast(String sql, Page page, Map<String, Object> params);
	
    Page queryPageCount(Page page, String sql, Object... args);
    Page queryPageList(Page page, String sql, Object... args);



    //void delete(ad_comment , String , Long );
}
