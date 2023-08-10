package BookLink.BookLink.Domain.MyPage;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class MyBookDto {

    private Integer record_cnt;
    private Integer rent_available_cnt;
    private Integer lend_cnt;
    private Integer rent_cnt;

    private List<MyBookRentDto> myBookRentDtoList = new ArrayList<>();
    private List<MyRecordBookDto> myRecordBookDtoList = new ArrayList<>();

    @Builder
    public MyBookDto(Integer record_cnt, Integer rent_available_cnt, Integer lend_cnt, Integer rent_cnt, List<MyBookRentDto> myBookRentDtoList,
                     List<MyRecordBookDto> myRecordBookDtoList) {
        this.record_cnt = record_cnt;
        this.rent_available_cnt = rent_available_cnt;
        this.lend_cnt = lend_cnt;
        this.rent_cnt = rent_cnt;
        this.myBookRentDtoList = myBookRentDtoList;
        this.myRecordBookDtoList = myRecordBookDtoList;
    }
}
