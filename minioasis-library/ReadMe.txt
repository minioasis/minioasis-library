===============================
MinIO server settings
===============================
1. Run MinIO as a service. (https://github.com/minio/minio-service)
	
	winsw is a wrapper to run any executable as an Windows service. (https://github.com/kohsuke/winsw)
	Download WinSW.NET4.exe (https://github.com/kohsuke/winsw/releases/download/winsw-v2.2.0/WinSW.NET4.exe)
	Rename the WinSW.NET4.exe to minio-service.exe
	Create a xml file minio-service.xml insert the configuration below
	Open a cmd as Administrator and execute minio-service.exe install
	
2. application.properties - MinIO setting

	minio.endpoint = http://localhost:9000
	minio.accessKey = LQ2Y0QOWBH7CLN9586SF
	minio.secretKey = JUi5+0XP39fW6Kqsfa7aC2Kq5tPci9xgXaeZQNHD
	minio.biblio.bucket.name = biblio
	minio.biblio.thumbnail.bucket.name = biblio-thumbnail
	minio.patron.bucket.name = patron
	minio.patron.thumbnail.bucket.name = patron-thumbnail
	
	after configuration is done, please create buckets :
	
	"biblio", 
	"biblio-thumbnail", 
	"patron", 
	"patron-thumbnail"
	
	in MinIO server !
	
================================	
Database connection settings
================================

setting your database connection in "application.properties" (located at src/main/resources)

 spring.datasource.url = jdbc:mariadb://localhost:3306/(your database name)
 (example : jdbc:mariadb://localhost:3306/minioasis)
 
 spring.datasource.username = root (your username)
 
 spring.datasource.password = 1674584 (your password)

===============================
Library setting
===============================

shelfmark.column = 7
image.destination.folder = E:\/minio\/data\/biblio (your minio data folder "biblio" path)

================================
Telegram settings
================================

telegrambot.token = 984832545:AAH0QnOUnxUYEV_8ro4CqLmY_sHyx10c9gU (your telegram token)
telegrambot.username = minioasistest_bot (your bot name)
reminder.days = 3 (how many days should the reminder send before the due date ?)
