package com.healthMonitor.fall2020.orm;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;


/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-8-17
 * Time: 下午7:45
 * To change this template use File | Settings | File Templates.
 */
public class BaseSqlTemplateImpl extends JdbcDaoSupport implements IDataDao {
	/*
    private static final Logger log = LoggerFactory
            .getLogger(BaseSqlTemplateImpl.class);
    */
	protected IDataDao backupDao;
	
    protected SimpleJdbcInsert getSimpleJdbcInsert() {
        return new SimpleJdbcInsert(getJdbcTemplate());
    }

    protected SimpleJdbcCall getSimpleJdbcCall() {
        return new SimpleJdbcCall(getJdbcTemplate());
    }

    protected NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(getJdbcTemplate());
    }


    public  static void main(String [] args)
    {

/*
        Table table =  Test.class.getAnnotation(Table.class);

        if(table==null||table.name()==null||table.name().equals(""))
        {
            System.out.println(Test.class.getSimpleName());
        }
        else
        {
            System.out.println( table.name());
        }
        */

    }

	@Override
	public void setBackupDao(IDataDao dao) {
		backupDao=dao;
	}

    public <T> int add(T entity) {
    	if(backupDao!=null)
    	{
    		backupDao.add(entity);
    	}

        SimpleJdbcInsert insertActor = getSimpleJdbcInsert();
        insertActor.setTableName(tablename(entity.getClass()));
        BeanPropertySqlParameterSource source=new BeanPropertySqlParameterSource(entity);
        return insertActor.execute(source);
    }


    public <T> int batchAdd(Collection<T> entityCollection) {
    	if(backupDao!=null)
    	{
    		backupDao.batchAdd(entityCollection);
    	}
        if (entityCollection == null || entityCollection.size() == 0) return 0;
        SimpleJdbcInsert insertActor = getSimpleJdbcInsert();
        T entity = entityCollection.iterator().next();
        insertActor.setTableName(tablename(entity.getClass()));
        SqlParameterSource[] batchArgs = SqlParameterSourceUtils.createBatch(entityCollection.toArray());
        int[] result = insertActor.executeBatch(batchArgs);
        return result.length;
    }


    public <T , PK extends Serializable> int delete(PK id, Class<T> entityClass) {
    	if(backupDao!=null)
    	{
    		backupDao.delete(id,entityClass);
    	}
        return batchDelete(new Serializable[]{id}, entityClass);
    }


    public <T , PK extends Serializable> int batchDelete(final PK[] ids, Class<T> entityClass) {
    	if(backupDao!=null)
    	{
    		backupDao.batchDelete(ids,entityClass);
    	}
        if (ids == null || ids.length == 0) return 0;
        String idName=getTableIDName(entityClass) ;
        StringBuilder builder = new StringBuilder();
        builder.append("DELETE FROM ").append(tablename(entityClass)).append(" WHERE "+idName+" = ?");
       // log.debug("[Batch Delete] %s, %s", builder.toString(), ids);
        int[] result = getJdbcTemplate().batchUpdate(builder.toString(), new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setObject(1, ids[i]);
            }

            @Override
            public int getBatchSize() {
                return ids.length;
            }
        });
        return result.length;
    }


    public <T > int delete(Class<T> entityClass, String where, Object... args) {
    	if(backupDao!=null)
    	{
    		backupDao.delete(entityClass,where,args);
    	}
        StringBuilder builder = new StringBuilder();
        builder.append("DELETE FROM ").append(tablename(entityClass));
        if (!Strings.isNullOrEmpty(where)) builder.append(" ").append(where);
      //  log.debug("[Delete] %s, %s", builder.toString(), args);
        return getJdbcTemplate().update(builder.toString(), args);
    }


    public <T > int delete(Class<T> entityClass, String where, Map<String, Object> params) {
    	if(backupDao!=null)
    	{
    		backupDao.delete(entityClass,where,params);
    	}
        StringBuilder builder = new StringBuilder();
        builder.append("DELETE FROM ").append(tablename(entityClass));
        if (!Strings.isNullOrEmpty(where)) builder.append("  ").append(where);
      //  log.debug("[Delete] %s, %s", builder.toString(), params);
        return getNamedParameterJdbcTemplate().update(builder.toString(), params);
    }


    public <T > int update(T entity, Class<T> entityClass) {
    	if(backupDao!=null)
    	{
    		backupDao.update(entity,entityClass);
    	}
        List<T> entities = Lists.newArrayList();
        entities.add(entity);
        return batchUpdate(entities, entityClass);
    }


    public <T > int update(T entity) {
    	if(backupDao!=null)
    	{
    		backupDao.update(entity);
    	}
        List<T> entities = Lists.newArrayList();
        entities.add(entity);
        return batchUpdate(entities, (Class<T>) entity.getClass());
    }


    public <T > int batchUpdate(Collection<T> entityCollection, Class<T> entityClass) {
    	if(backupDao!=null)
    	{
    		backupDao.batchUpdate(entityCollection,entityClass);
    	}
        if (entityCollection == null || entityCollection.size() == 0) return 0;
        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE ").append(tablename(entityClass)).append(" SET ");
        Set<Field> fields = BaseReflection.getFields(entityClass);
        String idName="";
        for (Field field : fields) {
            String fieldName = field.getName();
            Class<?> fieldType = field.getType();
            Boolean bId=isId(field);
            if(bId==true) idName= fieldName;
            if (bId==false &&
                    !"serialVersionUID".equalsIgnoreCase(fieldName) &  
                    // 判断是否为基本类型
                    (Long.class.equals(fieldType)
                            || String.class.equals(fieldType)
                            || Integer.class.equals(fieldType)
                            ||  Long.class.equals(fieldType)
                            ||  long.class.equals(fieldType)
                            ||  double.class.equals(fieldType)
                            ||  int.class.equals(fieldType)
                            ||  float.class.equals(fieldType)
                            ||  Float.class.equals(fieldType)
                            || Double.class.equals(fieldType)
                            // 重复判断，故意的？ -- by kakin 2014/5/13
                            || Timestamp.class.equals(fieldType)
                            || Date.class.equals(fieldType)
//                            || Long.class.equals(fieldType)
                            || Boolean.class.equals(fieldType)
                            || Enum.class.equals(fieldType)
                            || BigDecimal.class.equals(fieldType))
                    ) {
                builder.append(fieldName).append(" = :").append(fieldName).append(", ");
            }
        }

        // 去尾“,”
       builder.replace(builder.lastIndexOf(","), builder.length(), "");
      //  builder.append(String.format("updateAt = %s", System.currentTimeMillis()));
        builder.append(" WHERE "+idName+" = :"+idName);
      //  log.debug("[Batch Update] %s", builder.toString());
        SqlParameterSource[] batchArgs = SqlParameterSourceUtils.createBatch(entityCollection.toArray());
        int[] result = getNamedParameterJdbcTemplate().batchUpdate(builder.toString(), batchArgs);
        return result.length;
    }

    public  String getTableIDName(Class entityClass) {

        String ret="";
        Set<Field> fields = BaseReflection.getFields(entityClass);
        for (Field field : fields)
        {

            Annotation [] annotations= field.getAnnotations() ;
            if(annotations!=null)
            {
                for(int i=0;i<annotations.length;i++)
                {
                    Annotation annotation=  annotations[i];

                    if(annotation.annotationType().equals(Id.class))
                    {
                        ret=field.getName();

                    }
                }
            }
        }
        return  ret;
        // return (table == null) ? this.getClass().getName().replace(".", "_").toUpperCase() : table.getName();
    }
    public <T , PK extends Serializable> T get(PK id, Class<T> entityClass) {
        StringBuilder builder = new StringBuilder();
        String idName=getTableIDName(entityClass) ;
        builder.append("SELECT ")
                .append(" * ")
                .append(" FROM ").append(tablename(entityClass)).append(" WHERE "+idName+" = ? LIMIT 1");
        try {
            return getJdbcTemplate().queryForObject(builder.toString(), BeanPropertyRowMapper.newInstance(entityClass), id);
        } catch (Exception ex) {
        	if(!((ex instanceof IncorrectResultSizeDataAccessException)
                    && ((IncorrectResultSizeDataAccessException) ex).getActualSize() == 0)) ex.printStackTrace();
        	return null;
        }
    }


    public <T> T get(Class<T> entityClass, String where, Object... args) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT ")
                .append( "*" )
                .append(" FROM ").append(tablename(entityClass)).append("  ").append(where).append(" LIMIT 1");
       // log.debug("[Get] %s, %s", builder.toString(), args);
        try {
            return getJdbcTemplate().queryForObject(builder.toString(), BeanPropertyRowMapper.newInstance(entityClass), args);
        } catch (Exception ex) {
        	if(!((ex instanceof IncorrectResultSizeDataAccessException)
                    && ((IncorrectResultSizeDataAccessException) ex).getActualSize() == 0)) ex.printStackTrace();
            return null;
        }
    }


    public <T> T get(Class<T> entityClass, String where, Map<String, Object> params) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT ")
                .append(" * ")
                .append(" FROM ").append(tablename(entityClass)).append("  ").append(where).append(" LIMIT 1");
     //   log.debug("[Get] %s, %s", builder.toString(), params);
        try {
            return getNamedParameterJdbcTemplate().queryForObject(builder.toString(), params, BeanPropertyRowMapper.newInstance(entityClass));
        } catch (EmptyResultDataAccessException e) {
        	if(!((e instanceof IncorrectResultSizeDataAccessException)
                    && ((IncorrectResultSizeDataAccessException) e).getActualSize() == 0)) e.printStackTrace();
            return null;
        }
    }


    public <T> long queryTotalRows(Class<T> entityClass, String where, Object... args) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(1) FROM ").append(tablename(entityClass));
        if (!Strings.isNullOrEmpty(where)) builder.append("  ").append(where);
     //   log.debug("[Query Total Rows] %s, %s", builder.toString(), args);

        return getJdbcTemplate().queryForObject(builder.toString(), args,Long.class);
    }


    public <T> long queryTotalRows(Class<T> entityClass, String where, Map<String, Object> params) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(1) FROM ").append(tablename(entityClass));
        if (!Strings.isNullOrEmpty(where)) builder.append("  ").append(where);

        return getNamedParameterJdbcTemplate().queryForObject(builder.toString(), params,Long.class);
    }



    public  long getCount(String sql, Object... args) {
       // StringBuilder builder = new StringBuilder();
    	
    	if(sql.toLowerCase().indexOf("for update")!=-1)
    	{
        	if(backupDao!=null)
        	{
        		backupDao.getCount(sql,args);
        	}
    	}
    	
        sql= "select count(1) from (" + SqlUtil.trimOrderBy(sql) + ") tnnyyyyyy";
        //   log.debug("[Query Total Rows] %s, %s", builder.toString(), args);
        return getJdbcTemplate().queryForObject(sql, args,Long.class);
    }

    public  long getCount(String sql, Map<String, Object> params) {
    	if(sql.toLowerCase().indexOf("for update")!=-1)
    	{
        	if(backupDao!=null)
        	{
        		backupDao.getCount(sql,params);
        	}
    	}
    	
        sql= "select count(1) from (" + SqlUtil.trimOrderBy(sql) + ") tnnyyyyyy";
        //   log.debug("[Query Total Rows] %s, %s", builder.toString(), args);
        //return getJdbcTemplate().queryForObject(sql, params,Long.class);
        return getNamedParameterJdbcTemplate().queryForObject(sql, params,Long.class);
    	
    
    	
    }

    
    public  long getCountFast(String sql, Object... args) {
        // StringBuilder builder = new StringBuilder();
     	
     	if(sql.toLowerCase().indexOf("for update")!=-1)
     	{
         	if(backupDao!=null)
         	{
         		backupDao.getCount(sql,args);
         	}
     	}
     	/*
         sql= "select count(1) from (" + SqlUtil.trimOrderBy(sql) + ") tnnyyyyyy";
         //   log.debug("[Query Total Rows] %s, %s", builder.toString(), args);
         return getJdbcTemplate().queryForLong(sql, args);
         */
         
        sql=SqlUtil.trimOrderBy(sql);
     	String lower=sql.toLowerCase();
     	if(lower.indexOf("group by")!=-1)
     	{
     	       sql= "select count(1) from (" + sql + ") tnnyyyyyy";
     	        //   log.debug("[Query Total Rows] %s, %s", builder.toString(), args);
     	        return getJdbcTemplate().queryForObject(sql, args,Long.class);
     	}
     	else
     	{
     		int pos1=lower.indexOf("select");
     		int pos2=lower.indexOf("from");
     		if(pos1>0)
     		{
     			sql=sql.substring(0,pos1)+"select count(*) "+sql.substring(pos2);
     		}
     		else
     		{
     			sql="select count(*) "+sql.substring(pos2);
     		}
     		 return getJdbcTemplate().queryForObject(sql, args,Long.class);
     	}
         
     }

     public  long getCountFast(String sql, Map<String, Object> params) {
     	if(sql.toLowerCase().indexOf("for update")!=-1)
     	{
         	if(backupDao!=null)
         	{
         		backupDao.getCount(sql,params);
         	}
     	}
     	/*
         sql= "select count(1) from (" + SqlUtil.trimOrderBy(sql) + ") tnnyyyyyy";
         //   log.debug("[Query Total Rows] %s, %s", builder.toString(), args);
         return getJdbcTemplate().queryForLong(sql, params);
         */
        sql=SqlUtil.trimOrderBy(sql);
     	String lower=sql.toLowerCase();
     	if(lower.indexOf("group by")!=-1)
     	{
     	       sql= "select count(1) from (" + sql + ") tnnyyyyyy";
     	        //   log.debug("[Query Total Rows] %s, %s", builder.toString(), args);
     	      //  return getJdbcTemplate().queryForLong(sql, params);
            return getNamedParameterJdbcTemplate().queryForObject(sql, params,Long.class);
     	}
     	else
     	{
     		int pos1=lower.indexOf("select");
     		int pos2=lower.indexOf("from");
     		if(pos1>0)
     		{
     			sql=sql.substring(0,pos1)+"select count(*) "+sql.substring(pos2);
     		}
     		else
     		{
     			sql="select count(*) "+sql.substring(pos2);
     		}
     		// return getJdbcTemplate().queryForLong(sql, params);
            return getNamedParameterJdbcTemplate().queryForObject(sql, params,Long.class);
     	}
         
     	
     
     	
     }
    

    public  String uniqueResult(String sql, Object... args) {
        // StringBuilder builder = new StringBuilder();

        //   log.debug("[Query Total Rows] %s, %s", builder.toString(), args);
       	if(sql.toLowerCase().indexOf("for update")!=-1)
    	{
        	if(backupDao!=null)
        	{
        		backupDao.uniqueResult(sql,args);
        	}
    	}
    	
        List<Map<String, Object>> list= getJdbcTemplate().queryForList(sql, args) ;
        if(list==null)
        {
            return "";
        }
        else if(list.size()>0)
        {
            Map<String, Object> map=list.get(0);
            Iterator keys = map.keySet().iterator();
            if(keys.hasNext())
            {
                String key = (String)keys.next();
               if(map.get(key)==null)
               {
                   return null;
               }
                return   map.get(key).toString();
            }
        }
        return "";
    }

    public  String uniqueResult(String sql, Map<String, Object> params) {
       	if(sql.toLowerCase().indexOf("for update")!=-1)
    	{
        	if(backupDao!=null)
        	{
        		backupDao.uniqueResult(sql,params);
        	}
    	}

        List<Map<String, Object>> list= getJdbcTemplate().queryForList(sql, params) ;
        if(list==null)
        {
            return "";
        }
        else if(list.size()>0)
        {
            Map<String, Object> map=list.get(0);
            Iterator keys = map.keySet().iterator();
            if(keys.hasNext())
            {
                String key = (String)keys.next();
                return   map.get(key).toString();
            }
        }
        return "";
    }






    public <T> List<T> query(Class<T> entityClass) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM ").append(tablename(entityClass));
       // log.debug("[Query] %s", builder.toString());
        List<T> rowset = getJdbcTemplate().query(
                builder.toString(),
                BeanPropertyRowMapper.newInstance(entityClass)
        );
        if (null == rowset) rowset = Lists.newArrayList();
        return rowset;
    }


    public <T> List<T> query(Class<T> entityClass, String where, Object... args) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT ")
                .append(" * ")
                .append(" FROM ")
                .append(tablename(entityClass))
                .append(" ")
             //   .append(CharMatcher.is('?').matchesAnyOf(where) ? " WHERE " : " ")
                .append(where);
       // log.debug("[Query] %s, %s", builder.toString(), args);

        List<T> rowset = getJdbcTemplate().query(builder.toString(), BeanPropertyRowMapper.newInstance(entityClass), args);
        if (null == rowset) rowset = Lists.newArrayList();
        return rowset;
    }


    public <T> List<T> query(Class<T> entityClass, String where, Map<String, Object> params) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT ")
                .append("*")
                .append(" FROM ").append(tablename(entityClass)).append(" ").append(where);

       // log.debug("[Query] %s, %s", builder.toString(), params);

        List<T> rowset = getNamedParameterJdbcTemplate().query(builder.toString(), params, BeanPropertyRowMapper.newInstance(entityClass));

        if (null == rowset) rowset = Lists.newArrayList();
        return rowset;
    }

//    @Override
//    public <T extends BaseBean> Pagination<T> query(Class<T> entityClass, MysqlPageable pageable) {
//        StringBuilder where = new StringBuilder("");
//        Set<String> conditions = pageable.getConditionKeys();
//        if (conditions.size() > 0)
//            where.append(" WHERE ").append(Joiner.on(" AND ").join(conditions));
//
//        StringBuilder builder = new StringBuilder();
//        builder.append("SELECT COUNT(1) FROM ").append(tablename(entityClass)).append(where);
//        long totalRows = 0;
//        if (pageable.getNamed()) {
//            totalRows = getNamedParameterJdbcTemplate().queryForLong(builder.toString(), pageable.getConditionParams());
//        } else {
//            totalRows = getJdbcTemplate().queryForLong(builder.toString(), pageable.getConditionValues().toArray());
//        }
//        log.debug("[Query Total %s] %s, %s", totalRows, builder.toString(), pageable);
//
//        int size = pageable.getSize();
//        int page = pageable.getPage();
//        int limit = size * (page - 1);
//
//        List<T> rowset = Lists.newLinkedList();
//
//        builder = new StringBuilder();
//        builder.append("SELECT ");
//        Set<String> includes = Sets.newLinkedHashSet();
//        if (includes.size() > 0)
//            builder.append(Joiner.on(",").join(includes));
//        else
//            builder.append(Joiner.on(",").join(AladdinReflection.getFieldNames(entityClass)));
//        builder.append(" FROM ").append(tablename(entityClass)).append(where);
//
//        Map<String, MysqlOrder> sorts = pageable.getSorts();
//        if (sorts.size() > 0)
//            builder.append(" ORDER BY ").append(Joiner.on(",").withKeyValueSeparator(" ").join(sorts));
//        if (pageable.getNamed()) {
//            builder.append(" LIMIT :limit, :size");
//            Map<String, Object> args = pageable.getConditionParams();
//            args.put("limit", limit);
//            args.put("size", size);
//            log.debug("[Query] %s, %s", builder.toString(), args);
//            rowset = getNamedParameterJdbcTemplate().query(
//                    builder.toString(),
//                    args,
//                    BeanPropertyRowMapper.newInstance(entityClass)
//            );
//        } else {
//            builder.append(" LIMIT ?, ?");
//            List<Object> args = pageable.getConditionValues();
//            args.add(limit);
//            args.add(size);
//            log.debug("[Query] %s, %s", builder.toString(), args);
//            rowset = getJdbcTemplate().query(
//                    builder.toString(),
//                    BeanPropertyRowMapper.newInstance(entityClass),
//                    args.toArray()
//            );
//        }
//        return new Pagination<T>(pageable, rowset, totalRows);
//    }


    public <T> boolean check(Class<T> entityClass, String key, Object value) {
        StringBuilder builder = new StringBuilder();
        builder.append(key).append(" = ?");
        long totalRows = queryTotalRows(entityClass, builder.toString(), value);
   //     log.debug("[Check] %s", totalRows);
        return totalRows > 0;
    }







//
//    @Override
//    public <T extends AladdinMapper> Pagination<T> query(Class<T> mapperClass, RowMapper<T> rowMapper, MysqlPageable pageable) {
//        String relationship = relationship(mapperClass);
//        StringBuilder where = new StringBuilder();
//        Set<String> conditions = pageable.getConditionKeys();
//        if (conditions.size() > 0)
//            where.append(" WHERE ").append(Joiner.on(" AND ").join(conditions));
//
//        StringBuilder builder = new StringBuilder();
//        builder.append("SELECT COUNT(1) FROM ").append(relationship).append(where);
//        Long totalRows = getJdbcTemplate().queryForLong(builder.toString(), pageable.getConditionValues().toArray());
//        log.debug("[Query Total %s] %s", totalRows, builder.toString());
//
//        int size = pageable.getSize();
//        int page = pageable.getPage();
//        int limit = size * (page - 1);
//
//        List<T> rowset = Lists.newLinkedList();
//
//        builder = new StringBuilder();
//        builder.append("SELECT ");
//        Set<String> includes = pageable.getIncludes();
//        if (includes.size() > 0)
//            builder.append(Joiner.on(",").join(includes));
//        else
//            builder.append(Joiner.on(",").join(AladdinReflection.getFieldNames(mapperClass)));
//        builder.append(" FROM ").append(relationship).append(where);
//
//        Map<String, MysqlOrder> sorts = pageable.getSorts();
//        if (sorts.size() > 0)
//            builder.append(" ORDER BY ").append(Joiner.on(",").withKeyValueSeparator(" ").join(sorts));
//        if (pageable.getNamed()) {
//            builder.append(" LIMIT :limit, :size");
//            Map<String, Object> args = pageable.getConditionParams();
//            args.put("limit", limit);
//            args.put("size", size);
//            log.debug("[Query] %s, %s", builder.toString(), args);
//            rowset = getNamedParameterJdbcTemplate().query(
//                    builder.toString(),
//                    args,
//                    rowMapper
//            );
//        } else {
//            builder.append(" LIMIT ?, ?");
//            List<Object> args = pageable.getConditionValues();
//            args.add(limit);
//            args.add(size);
//            log.debug("[Query] %s, %s", builder.toString(), args);
//            rowset = getJdbcTemplate().query(
//                    builder.toString(),
//                    rowMapper,
//                    args.toArray()
//            );
//        }
//        return new Pagination<T>(pageable, rowset, totalRows);
//    }


    public <T> T getBySql(String sql,Class entityClass , Object... args) {
        //  log.debug("[Query By Sql] %s, %s", sql, args);
    	
       	if(sql.toLowerCase().indexOf("for update")!=-1)
    	{
        	if(backupDao!=null)
        	{
        		backupDao.getBySql(sql,entityClass,args);
        	}
    	}
    	
          try {
              return (T) getJdbcTemplate().queryForObject(sql, BeanPropertyRowMapper.newInstance(entityClass), args);
          } catch (Exception ex) {
        	  if(!(ex instanceof EmptyResultDataAccessException)){
//        		  System.out.println("BaseSqlTemplateImpl.getBySql(sql,entityClass,args) 执行出错。sql = "+sql);
            	  ex.printStackTrace();
        	  }
        	  if(!((ex instanceof IncorrectResultSizeDataAccessException)
                      && ((IncorrectResultSizeDataAccessException) ex).getActualSize() == 0)) ex.printStackTrace();
              return null;
          }
      }
    
    public <T> T getBySql(String sql, RowMapper<T> rowMapper, Object... args) {
      //  log.debug("[Query By Sql] %s, %s", sql, args);
    	
       	if(sql.toLowerCase().indexOf("for update")!=-1)
    	{
        	if(backupDao!=null)
        	{
        		backupDao.getBySql(sql,rowMapper,args);
        	}
    	}
    	
        try {
            return getJdbcTemplate().queryForObject(sql, rowMapper, args);
        } catch (Exception ex) {
        	if(!((ex instanceof IncorrectResultSizeDataAccessException)
                    && ((IncorrectResultSizeDataAccessException) ex).getActualSize() == 0)) ex.printStackTrace();
            return null;
        }
    }


    public <T> T getBySql(String sql, RowMapper<T> rowMapper, Map<String, Object> params) {
      //  log.debug("[Query By Sql] %s, %s", sql, params);
       	if(sql.toLowerCase().indexOf("for update")!=-1)
    	{
        	if(backupDao!=null)
        	{
        		backupDao.getBySql(sql,rowMapper,params);
        	}
    	}
    	
        try {
            return getNamedParameterJdbcTemplate().queryForObject(sql, params, rowMapper);
        } catch (Exception ex) {
        	if(!((ex instanceof IncorrectResultSizeDataAccessException)
                    && ((IncorrectResultSizeDataAccessException) ex).getActualSize() == 0)) ex.printStackTrace();
            return null;
        }
    }


    public <T> List<T> queryBySql(String sql, RowMapper<T> rowMapper, Object... args) {
       // log.debug("[Query By Sql] %s, %s", sql, args);
    	
       	if(sql.toLowerCase().indexOf("for update")!=-1)
    	{
        	if(backupDao!=null)
        	{
        		backupDao.queryBySql(sql,rowMapper,args);
        	}
    	}
    	
        List<T> rowset = getJdbcTemplate().query(sql, rowMapper, args);
       // if (null == rowset) rowset = Lists.newArrayList();
        return rowset;
    }




    public <T> List<T> queryBySql(String sql, Class entityClass , Object... args) {
        // log.debug("[Query By Sql] %s, %s", sql, args);
       	if(sql.toLowerCase().indexOf("for update")!=-1)
    	{
        	if(backupDao!=null)
        	{
        		backupDao.queryBySql(sql,entityClass,args);
        	}
    	}
    	
        List<T> rowset = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(entityClass), args);
       // if (null == rowset) rowset = Lists.newArrayList();
        return rowset;
    }


    //  BeanPropertyRowMapper.newInstance(entityClass)


    public <T> List<T> queryBySql(String sql, RowMapper<T> rowMapper, Map<String, Object> params) {

       	if(sql.toLowerCase().indexOf("for update")!=-1)
    	{
        	if(backupDao!=null)
        	{
        		backupDao.queryBySql(sql,rowMapper,params);
        	}
    	}
    	
        List<T> rowset = getNamedParameterJdbcTemplate().query(sql, params, rowMapper);
       // if (null == rowset) rowset = Lists.newArrayList();
        return rowset;
    }


    public <T> List<T> queryBySql(String sql, Class entityClass , Map<String, Object> params) {
       	if(sql.toLowerCase().indexOf("for update")!=-1)
    	{
        	if(backupDao!=null)
        	{
        		backupDao.queryBySql(sql,entityClass,params);
        	}
    	}
    	
    	
        List<T> rowset = getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(entityClass));
      //  if (null == rowset) rowset = Lists.newArrayList();
        return rowset;
    }



    public <T> int truncate(Class<T> entityClass) {
    	if(backupDao!=null)
    	{
    		backupDao.truncate(entityClass);
    	}
        StringBuilder builder = new StringBuilder();
        builder.append("TRUNCATE TABLE ").append(tablename(entityClass));
      //  log.debug("[Query] %s", builder.toString());
        return getJdbcTemplate().update(builder.toString());
    }


   
    public void executeSql(String sql) {
       // log.debug("[Execute Sql] %s", sql);
    	//System.out.println(sql+"............");
    	if(backupDao!=null)
    	{
    		backupDao.executeSql(sql);
    	}
        getJdbcTemplate().execute(sql);
    }


    public int executeSql(String sql, Object... args) {
      //  log.debug("[Execute Sql] %s,%s", sql, args);
    	if(backupDao!=null)
    	{
    		backupDao.executeSql(sql,args);
    	}
        return getJdbcTemplate().update(sql, args);
    }


    public int executeSql(String sql, Map<String, Object> params) {
       // log.debug("[Execute Sql] %s,%s", sql, params);
       	if(backupDao!=null)
    	{
    		backupDao.executeSql(sql,params);
    	}
        return getNamedParameterJdbcTemplate().update(sql, params);
    }


    public Map<String, Object> queryBySql(String sql, Object... args) {
      
       	if(sql.toLowerCase().indexOf("for update")!=-1)
    	{
        	if(backupDao!=null)
        	{
        		backupDao.queryBySql(sql,args);
        	}
    	}
    	
        Map<String, Object> map = null;
        try{  
        	map = getJdbcTemplate().queryForMap(sql, args);
        }catch (EmptyResultDataAccessException e) {  
        	return null;
        } catch (Exception e) {
        	throw new RuntimeException(e);
        }
        //if (null == map) map = Maps.newHashMap();
        return map;
    }


    public Map<String, Object> queryBySql(String sql, Map<String, Object> params) {

       	if(sql.toLowerCase().indexOf("for update")!=-1)
    	{
        	if(backupDao!=null)
        	{
        		backupDao.queryBySql(sql,params);
        	}
    	}
    	
        Map<String, Object> map = getNamedParameterJdbcTemplate().queryForMap(sql, params);
       // if (null == map) map = Maps.newHashMap();
        return map;
    }


    public List<Map<String, Object>> queryMap(String sql, Object... args) {
     
       	if(sql.toLowerCase().indexOf("for update")!=-1)
    	{
        	if(backupDao!=null)
        	{
        		backupDao.queryMap(sql,args);
        	}
    	}
    	
        List<Map<String, Object>> rowset = getJdbcTemplate().queryForList(sql, args);
       // if (null == rowset) rowset = Lists.newArrayList();
        return rowset;
    }


    public List<Map<String, Object>> queryMap(String sql, Map<String, Object> params) {

       	if(sql.toLowerCase().indexOf("for update")!=-1)
    	{
        	if(backupDao!=null)
        	{
        		backupDao.queryMap(sql,params);
        	}
    	}
       	
        List<Map<String, Object>> rowset = getNamedParameterJdbcTemplate().queryForList(sql, params);
     //   if (null == rowset) rowset = Lists.newArrayList();
        return rowset;
    }

    /*------辅助方法-----*/
    public <T> String tablename(Class<T> entityClass) {
        Table table = entityClass.getAnnotation(Table.class);
            if(table==null||table.name()==null||table.name().equals(""))
            {
                return   entityClass.getSimpleName();
            }
        else
            {
                return    table.name();
            }
       // return (table == null) ? this.getClass().getName().replace(".", "_").toUpperCase() : table.getName();
    }


    public  Boolean isId(Field field ) {
        Boolean ret=false;
        Annotation [] annotations= field.getAnnotations() ;
         if(annotations!=null)
         {
             for(int i=0;i<annotations.length;i++)
             {
                 Annotation annotation=  annotations[i];
                 if(annotation.annotationType().equals(Id.class))
                 {
                     ret=true;

                 }
             }
         }
        return  ret;
        // return (table == null) ? this.getClass().getName().replace(".", "_").toUpperCase() : table.getName();
    }



    public  <T> Page queryList(Page page,String sql ,Class<T> entityClass,
                                 Object... args) {

        long count= getCount(sql,args);

        long PageCount = count / page.getmPageSize() + (count% page.getmPageSize()==0?0:1);

        page.setmPageCount(PageCount);
        if (page.getmPageNow() > PageCount) {
            page.setmPageNow(PageCount);
        }
        if (page.getmPageNow() < 1) {
            page.setmPageCount(1);
        }
        page.setmPagePre(page.getmPageNow() - 1);
        if (page.getmPagePre() < 1) {
            page.setmPagePre(1);
        }

        page.setmPageNext(page.getmPageNow() + 1);
        if (page.getmPageNext() > PageCount) {
            page.setmPageNext(page.getmPageNow());
        }

        page.setmToltalSize(count);

        page.setmPageLast(PageCount);


        long limit = page.getmPageSize() * (page.getmPageNow() - 1);
        sql=sql+" Limit "+ limit+","+page.getmPageSize();
        List<T> rowset = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(entityClass), args);
      //  if (null == rowset) rowset = Lists.newArrayList();

        page.setmList(rowset);
        return page;
    }

    public  <T> Page queryList(Page page,String sql ,Class<T> entityClass,
                                                Map<String, Object> params) {

        long count= getCount(sql,params);

        long PageCount = count / page.getmPageSize() + (count% page.getmPageSize()==0?0:1);

        page.setmPageCount(PageCount);
        if (page.getmPageNow() > PageCount) {
            page.setmPageNow(PageCount);
        }
        if (page.getmPageNow() < 1) {
            page.setmPageCount(1);
        }
        page.setmPagePre(page.getmPageNow() - 1);
        if (page.getmPagePre() < 1) {
            page.setmPagePre(1);
        }

        page.setmPageNext(page.getmPageNow() + 1);
        if (page.getmPageNext() > PageCount) {
            page.setmPageNext(page.getmPageNow());
        }

        page.setmToltalSize(count);

        page.setmPageLast(PageCount);


        long limit = page.getmPageSize() * (page.getmPageNow() - 1);
        sql=sql+" Limit "+ limit+","+page.getmPageSize();
        List<T> rowset = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(entityClass), params);
        //if (null == rowset) rowset = Lists.newArrayList();

        page.setmList(rowset);
        return page;
    }




    public Page queryMap(Page page,String sql ,Object... args) {
    	// 兼容不分页查询结果情况
    	if (page.ignorePage()) {
    		page.setmList(queryMap(sql, args));
    		return page;
    	}

        long count= getCount(sql,args);

        long PageCount = count / page.getmPageSize() + (count% page.getmPageSize()==0?0:1);

        page.setmPageCount(PageCount);
        if (page.getmPageNow() > PageCount) {
            page.setmPageNow(PageCount);
        }
        if (page.getmPageNow() < 1) {
            page.setmPageCount(1);
        }
        page.setmPagePre(page.getmPageNow() - 1);
        if (page.getmPagePre() < 1) {
            page.setmPagePre(1);
        }

        page.setmPageNext(page.getmPageNow() + 1);
        if (page.getmPageNext() > PageCount) {
            page.setmPageNext(page.getmPageNow());
        }

        page.setmToltalSize(count);

        page.setmPageLast(PageCount);


        long limit = page.getmPageSize() * (page.getmPageNow() - 1);
        sql=sql+" Limit "+ limit+","+page.getmPageSize();
        List<Map<String, Object>> rowset = getJdbcTemplate().queryForList(sql, args);
        if (null == rowset) rowset = Lists.newArrayList();

        page.setmList(rowset);
        return page;
    }

    public   Page queryMap(String sql, Page page,Map<String, Object> params) {

        long count= getCount(sql,params);

        long PageCount = count / page.getmPageSize() + (count% page.getmPageSize()==0?0:1);

        page.setmPageCount(PageCount);
        if (page.getmPageNow() > PageCount) {
            page.setmPageNow(PageCount);
        }
        if (page.getmPageNow() < 1) {
            page.setmPageCount(1);
        }
        page.setmPagePre(page.getmPageNow() - 1);
        if (page.getmPagePre() < 1) {
            page.setmPagePre(1);
        }

        page.setmPageNext(page.getmPageNow() + 1);
        if (page.getmPageNext() > PageCount) {
            page.setmPageNext(page.getmPageNow());
        }

        page.setmToltalSize(count);

        page.setmPageLast(PageCount);


        long limit = page.getmPageSize() * (page.getmPageNow() - 1);
        sql=sql+" Limit "+ limit+","+page.getmPageSize();
      //  List<T> rowset = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(entityClass), params);
        List<Map<String, Object>> rowset = getNamedParameterJdbcTemplate().queryForList(sql, params);
        if (null == rowset) rowset = Lists.newArrayList();

        page.setmList(rowset);
        return page;
    }

    


    public  <T> Page queryListForUpdate(Page page,String sql ,Class<T> entityClass,
                                                Object... args) {

        long count= getCount(sql,args);

        long PageCount = count / page.getmPageSize() + (count% page.getmPageSize()==0?0:1);

        page.setmPageCount(PageCount);
        if (page.getmPageNow() > PageCount) {
            page.setmPageNow(PageCount);
        }
        if (page.getmPageNow() < 1) {
            page.setmPageNow(1);
        }
        page.setmPagePre(page.getmPageNow() - 1);
        if (page.getmPagePre() < 1) {
            page.setmPagePre(1);
        }

        page.setmPageNext(page.getmPageNow() + 1);
        if (page.getmPageNext() > PageCount) {
            page.setmPageNext(page.getmPageNow());
        }

        page.setmToltalSize(count);

        page.setmPageLast(PageCount);


        long limit = page.getmPageSize() * (page.getmPageNow() - 1);
        sql=sql+" Limit "+ limit+","+page.getmPageSize();
        List<T> rowset = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(entityClass), args);
        if (null == rowset) rowset = Lists.newArrayList();

        page.setmList(rowset);
        return page;
    }


    public  <T> Page queryListForUpdate(Page page,String sql ,Class<T> entityClass,
                                                Map<String, Object> params) {

        long count= getCount(sql,params);

        long PageCount = count / page.getmPageSize() + (count% page.getmPageSize()==0?0:1);

        page.setmPageCount(PageCount);
        if (page.getmPageNow() > PageCount) {
            page.setmPageNow(PageCount);
        }
        if (page.getmPageNow() < 1) {
            page.setmPageNow(1);
        }
        page.setmPagePre(page.getmPageNow() - 1);
        if (page.getmPagePre() < 1) {
            page.setmPagePre(1);
        }

        page.setmPageNext(page.getmPageNow() + 1);
        if (page.getmPageNext() > PageCount) {
            page.setmPageNext(page.getmPageNow());
        }

        page.setmToltalSize(count);

        page.setmPageLast(PageCount);


        long limit = page.getmPageSize() * (page.getmPageNow() - 1);
        sql=sql+" Limit "+ limit+","+page.getmPageSize();
        List<T> rowset = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(entityClass), params);
        if (null == rowset) rowset = Lists.newArrayList();

        page.setmList(rowset);
        return page;
    }




    public   Page queryMapForUpdate(Page page,String sql ,Object... args) {

        long count= getCount(sql,args);

        long PageCount = count / page.getmPageSize() + (count% page.getmPageSize()==0?0:1);

        page.setmPageCount(PageCount);
        if (page.getmPageNow() > PageCount) {
            page.setmPageNow(PageCount);
        }
        if (page.getmPageNow() < 1) {
            page.setmPageNow(1);
        }
        page.setmPagePre(page.getmPageNow() - 1);
        if (page.getmPagePre() < 1) {
            page.setmPagePre(1);
        }

        page.setmPageNext(page.getmPageNow() + 1);
        if (page.getmPageNext() > PageCount) {
            page.setmPageNext(page.getmPageNow());
        }

        page.setmToltalSize(count);

        page.setmPageLast(PageCount);


        long limit = page.getmPageSize() * (page.getmPageNow() - 1);
        sql=sql+" Limit "+ limit+","+page.getmPageSize();
        List<Map<String, Object>> rowset = getJdbcTemplate().queryForList(sql, args);
        if (null == rowset) rowset = Lists.newArrayList();

        page.setmList(rowset);
        return page;
    }

    public   Page queryMapForUpdate(String sql, Page page,Map<String, Object> params) {

        long count= getCount(sql,params);

        long PageCount = count / page.getmPageSize() + (count% page.getmPageSize()==0?0:1);

        page.setmPageCount(PageCount);
        if (page.getmPageNow() > PageCount) {
            page.setmPageNow(PageCount);
        }
        if (page.getmPageNow() < 1) {
            page.setmPageNow(1);
        }
        page.setmPagePre(page.getmPageNow() - 1);
        if (page.getmPagePre() < 1) {
            page.setmPagePre(1);
        }

        page.setmPageNext(page.getmPageNow() + 1);
        if (page.getmPageNext() > PageCount) {
            page.setmPageNext(page.getmPageNow());
        }

        page.setmToltalSize(count);

        page.setmPageLast(PageCount);


        long limit = page.getmPageSize() * (page.getmPageNow() - 1);
        sql=sql+" Limit "+ limit+","+page.getmPageSize();
        //  List<T> rowset = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(entityClass), params);
        List<Map<String, Object>> rowset = getNamedParameterJdbcTemplate().queryForList(sql, params);
        if (null == rowset) rowset = Lists.newArrayList();

        page.setmList(rowset);
        return page;
    }

	@Override
	public <T> Page queryListFast(Page page, String sql, Class<T> entityClass,
			Object... args) {
		// TODO Auto-generated method stub
		return null;
	}


	public <T> Page queryListFast(Page page, String sql, Class<T> entityClass,
			Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}


	public Page queryMapFast(Page page, String sql, Object... args) {
		// TODO Auto-generated method stub
		return null;
	}


	public Page queryMapFast(String sql, Page page, Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}


	public <T> Page queryListForUpdateFast(Page page, String sql,
			Class<T> entityClass, Object[] args) {
		// TODO Auto-generated method stub
		return null;
	}


	public <T> Page queryListForUpdateFast(Page page, String sql,
			Class<T> entityClass, Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}


	public Page queryMapForUpdateFast(Page page, String sql, Object[] args) {
		// TODO Auto-generated method stub
		return null;
	}


	public Page queryMapForUpdateFast(String sql, Page page,
			Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page queryPageCount(Page page, String sql, Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page queryPageList(Page page, String sql, Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> int updateNew(T entity) {
    	if(backupDao!=null)
    	{
    		backupDao.updateNew(entity);
    	}
        List<T> entities = Lists.newArrayList();
        entities.add(entity);
        return batchUpdateNew(entities, (Class<T>) entity.getClass());
	}
    public <T > int batchUpdateNew(Collection<T> entityCollection, Class<T> entityClass) {
    	if(backupDao!=null)
    	{
    		backupDao.batchUpdateNew(entityCollection,entityClass);
    	}
        if (entityCollection == null || entityCollection.size() == 0) return 0;
        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE ").append(tablename(entityClass)).append(" SET ");
        Set<Field> fields = BaseReflection.getFields(entityClass);
        String idName="";
        for (Field field : fields) {
        	//有被javax.persistence.Transient注解修饰的属性，表示不在数据库表建立映射关系，过滤该属性
        	if(isTransient(field)){
        		continue;
        	}
            String fieldName = field.getName();
            Class<?> fieldType = field.getType();
            Boolean bId=isId(field);
            if(bId==true) idName= fieldName;
            if (bId==false &&
                    !"serialVersionUID".equalsIgnoreCase(fieldName) &  
                    // 判断是否为基本类型
                    (Long.class.equals(fieldType)
                            || String.class.equals(fieldType)
                            || Integer.class.equals(fieldType)
                            ||  Long.class.equals(fieldType)
                            ||  long.class.equals(fieldType)
                            ||  double.class.equals(fieldType)
                            ||  int.class.equals(fieldType)
                            ||  float.class.equals(fieldType)
                            ||  Float.class.equals(fieldType)
                            || Double.class.equals(fieldType)
                            // 重复判断，故意的？ -- by kakin 2014/5/13
                            || Timestamp.class.equals(fieldType)
                            || Date.class.equals(fieldType)
//                            || Long.class.equals(fieldType)
                            || Boolean.class.equals(fieldType)
                            || Enum.class.equals(fieldType)
                            || BigDecimal.class.equals(fieldType))
                    ) {
                builder.append(fieldName).append(" = :").append(fieldName).append(", ");
            }
        }

        // 去尾“,”
        builder.replace(builder.lastIndexOf(","), builder.length(), "");
        builder.append(" WHERE "+idName+" = :"+idName);
        SqlParameterSource[] batchArgs = SqlParameterSourceUtils.createBatch(entityCollection.toArray());
        int[] result = getNamedParameterJdbcTemplate().batchUpdate(builder.toString(), batchArgs);
        return result.length;
    }
    //判断该字段是否有被javax.persistence.Transient注解修饰
	private boolean isTransient(Field field) {
		Boolean ret = false;
		Annotation[] annotations = field.getAnnotations();
		if (annotations != null) {
			for (int i = 0; i < annotations.length; i++) {
				Annotation annotation = annotations[i];
				if (annotation.annotationType().equals(Transient.class)) {
					ret = true;
				}
			}
		}
		return ret;
	}
}
