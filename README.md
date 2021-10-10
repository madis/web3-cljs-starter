## REPL

Start REPL with
```
npx shadow-cljs clj-repl
```

Then depending if you want to work on the front-end or back-end, use:
```clojure
; For UI development (the app must be open in a browser window to have the JS runtime to connect to)
(shadow/repl :dev-ui)

; For back-end development
(shadow/repl :dev-server)
```
