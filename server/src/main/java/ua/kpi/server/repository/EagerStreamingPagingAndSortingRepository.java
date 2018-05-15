package ua.kpi.server.repository;

import com.google.common.collect.Streams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Repository with eacger streaming page by page
 */
@NoRepositoryBean
public interface EagerStreamingPagingAndSortingRepository<T, ID> extends ExtendedJpaRepository<T, ID>, PagingAndSortingRepository<T, ID> {

    /**
     * Fetches all pages starting from the current
     *
     * @param from page to start with
     * @return iterator that navigates through pages
     */
    default Iterator<Page<T>> fetchAllPagesStartingFrom(Pageable from) {
        final long totalItems = count();
        final long pagesCount = (totalItems / from.getPageSize()) + 1;
        return new Iterator<Page<T>>() {

            int currentPageNo = 0;

            Pageable current = from;

            @Override
            public boolean hasNext() {
                return currentPageNo < pagesCount;
            }

            @Override
            public Page<T> next() {
                final Page<T> result = findAll(current);

                current = current.next();
                currentPageNo++;

                return result;
            }
        };
    }

    /**
     * {@link EagerStreamingPagingAndSortingRepository#fetchAllPagesStartingFrom}
     */
    default Stream<Page<T>> fetchAllPagesStreamStartingFrom(Pageable from) {
        return Streams.stream(fetchAllPagesStartingFrom(from));
    }

}
