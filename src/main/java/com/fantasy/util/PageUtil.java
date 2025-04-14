package com.fantasy.util;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PageUtil {

    // 数据聚合场景下的手工分页
// 也适合EmptyList场景 Page{count=true, pageNum=1, pageSize=10, startRow=0, endRow=0, total=0, pages=0, reasonable=null, pageSizeZero=null}
    public static <E> PageInfo<E> manualPage(List<E> res, Integer pageNum, Integer pageSize) {
        if (res == null) {
            res = new ArrayList<>();
        }

//        int pageNumber = PageUtils.getPageNum(pageNum);
        int pageNumber = pageNum;
//        int sizePerPage = PageUtils.getPageSize(pageSize);
        int sizePerPage = pageSize;
        int totalSize = res.size();

        List<E> pageElements = res.stream().skip((long) (pageNumber - 1) * sizePerPage).limit(sizePerPage).collect(Collectors.toList()); // 分页

        PageInfo<E> page = new PageInfo<>();
        page.setPageNum(pageNumber);
        page.setPageSize(sizePerPage);
        page.setTotal(totalSize);
        page.setPages((totalSize / pageSize + ((totalSize % pageSize == 0) ? 0 : 1)));
        page.setList(pageElements);

        return page;
    }

}
