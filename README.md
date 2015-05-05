匿名コメントシステム
====

Overview

## Description
ログイン無し、ユーザーの区別無しで、匿名でコメント投稿して表示するシステムの、Web側のインターフェイスです。

## Demo
<http://app.9684.jp/acs>

## Requirement
Play Framework 2.3.x で作成しています。

## Install
1. Oracle JDK 8 以上をインストールしてください。（たぶん 6.x 以上なら問題ないはず）

1. [Typesafe Activator](https://www.typesafe.com/get-started) をインストールしてください。（たぶん 1.3.2 以上）

1. GitHub からプロジェクトを Clone したら、そのディレクトリで `activator` コンソールを起動してください。

1. activator コンソールで、`h2-browser`と打ち込むとブラウザで以下のような画面が開くと思うので、各項目この通りに入力してください。  
基本的にはデフォルトのままで良いですが、ユーザー名とパスワードはどちらも`acs`としてください。  
Play Framework に組み込まれている H2DB を使用するための設定です。  
![Alt h2-browser](https://raw.githubusercontent.com/ikd9684/AnonymousCommentSystem/master/h2-browser.png)

1. h2-browser が起動したら、[conf/script.sql](https://github.com/ikd9684/AnonymousCommentSystem/blob/master/conf/script.sql) にある Create Table を実行してください。  
insert 文はダミーデータですので入れなくとも問題ないです。

1. activator コンソールに戻り、`run`と打ち込むとアプリケーションが起動します。  
以下の <http://localhost:9000/acs> を開いてみてください。  
動かしてみるだけならここまでです。

1. Eclipse でこのアプリケーションを編集するには、activator コンソールで eclipse コマンドを実行してください。  
Eclipse プロジェクト用のファイルが生成されますので、Eclipse で“既存プロジェクトのインポート”を実施すれば編集可能になるはずです。


たぶん、これで大丈夫。  
それぞれの手順の詳細は割愛します。

## Licence

[MIT](https://github.com/tcnksm/tool/blob/master/LICENCE)

## Author

[ikd9684](https://github.com/ikd9684)
