package com.healthMonitor.fall2020.orm;

import com.google.common.collect.Lists;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-8-18
 * Time: 下午5:00
 * To change this template use File | Settings | File Templates.
 */
public class MysqlTemplateImpl extends BaseSqlTemplateImpl {

    public  <T> Page queryList(Page page,String sql ,Class<T> entityClass,
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

    public  <T> Page queryList(Page page,String sql ,Class<T> entityClass,
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




    public   Page queryMap(Page page,String sql ,Object... args) {
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
    /*
     * 查询分页相关数据：总页数、页码等
     */
    public Page queryPageCount(Page page,String sql ,Object... args) {
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
        return page;
    }
    /*
     * 查询分页数据。该方法在执行queryPageCount方法之后调用
     */
    public Page queryPageList(Page page,String sql ,Object... args) {
    	// 兼容不分页查询结果情况
    	if (page.ignorePage()) {
    		page.setmList(queryMap(sql, args));
    		return page;
    	}
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

    @Override
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



    @Override
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
    @Override
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
    
    
    
    
    public  <T> Page queryListFast(Page page,String sql ,Class<T> entityClass,
            Object... args) {
		
		long count= getCountFast(sql,args);
		
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

    public  <T> Page queryListFast(Page page,String sql ,Class<T> entityClass,
            Map<String, Object> params) {

		long count= getCountFast(sql,params);
		
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




	public   Page queryMapFast(Page page,String sql ,Object... args) {
		// 兼容不分页查询结果情况
		if (page.ignorePage()) {
		page.setmList(queryMap(sql, args));
		return page;
		}
		
		long count= getCountFast(sql,args);
		
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

	public   Page queryMapFast(String sql, Page page,Map<String, Object> params) {
	
		long count= getCountFast(sql,params);
		
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
	public  <T> Page queryListForUpdateFast(Page page,String sql ,Class<T> entityClass,
	            Object... args) {
	
		long count= getCountFast(sql,args);
		
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

	@Override
	public  <T> Page queryListForUpdateFast(Page page,String sql ,Class<T> entityClass,
	            Map<String, Object> params) {
	
		long count= getCountFast(sql,params);
		
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



	@Override
	public   Page queryMapForUpdateFast(Page page,String sql ,Object... args) {
	
		long count= getCountFast(sql,args);
		
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
	
	@Override
	public   Page queryMapForUpdateFast(String sql, Page page,Map<String, Object> params) {
	
		long count= getCountFast(sql,params);
		
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
    
    
}
