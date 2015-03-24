# RssReaderAss
  Liệt kê các feature sẽ hiện thực cho App - Mỗi feature sẽ tương ứng với 1 trang giao diện. Sau khi liệt kê ra sẽ lọc lại nên hiện thực những feature nào.
  - **Feature 1**: Splash screen - Hiển thị logo, tên app khi vào từ launcher.
  - **Feature 2**: Sử dụng layout **Tab**, có 2 Tab là **All Categories** và **My favorite**.
    - ~~All Categories: Những category có sẵn của App~~
    - ~~My Favorites: Những category người dùng thích add vào. (dạng ntn http://ninjamock.com/s/iioepr)~~
  - **Feature 3**: Sử dụng **filter** thay cho Feature 2 (ý của thánh Hưng)
  - **Feature 4**: Cho phép người dùng **Add Source** , Source có thể add vào một category đã có sẵn, hoặc cho phép người dùng tạo một category mới.
  - **Feature 5**: Hiển thị list news feed (tùy theo category hay filter ở trên là gì)
  - **Feature 6**: Chi tiết từng feed - tức là xem file HTML của nó trên WebView.
  - **Feature 7**: Quản lý feed, feed nào đã đọc thì lần mở app sau ko hiển thị hoặc được làm mờ. Một feed sau thời gian t bao nhiêu đó sẽ đc xóa khỏi database (t sẽ do người dùng định nghĩa, tuy nhiên ban đầu nó sẽ mang một giá trị mặc định do mình chọn).
  - **Feature 8**: mở rộng **Feature 4** thành quản lý source. Source (tức url của rss feed) sẽ cho phép người dùng thêm, xóa, sửa. Categories cũng cho phép người dùng thêm, xóa sửa.
  - **Feature 9**: mở rộng **Feature 6**: có thể ko dùng webview mà parse thẳng trang HTML của feed và hiển thị nội dung ngay trong app (dùng webview nó sẽ hiển thị phiên bản mobile của trang web).
  - **Feature 10**: nếu **Feature 9** được hiện thực, có thể cho người dùng tùy chọn thay đổi font chữ và màu nền của trang đọc feed.