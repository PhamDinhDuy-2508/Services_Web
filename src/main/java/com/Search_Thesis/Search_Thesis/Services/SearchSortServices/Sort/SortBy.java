package com.Search_Thesis.Search_Thesis.Services.SearchSortServices.Sort;

import java.util.List;

public interface SortBy<T> {

    List<T> sortWith(String standard, int pageNum);

}
