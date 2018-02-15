### Example Project

Usage of [re-frame](https://github.com/Day8/re-frame), [re-frame-trace](https://github.com/Day8/re-frame-trace), and the [shadow-cljs](https://github.com/thheller/shadow-cljs/) build tool.

**[Live Demo](https://mhuebert.github.io/shadow-re-frame/)** - https://mhuebert.github.io/shadow-re-frame/

----

[re-frame-trace](https://github.com/Day8/re-frame-trace) provides visibility into `re-frame` applications, showing you exactly what's going on under the hood. 

[shadow-cljs](https://github.com/thheller/shadow-cljs/) is a fairly new-to-the-world (but used by @thheller for some years already) ClojureScript build tool. It's improving day by day. It does some nice things, for example caching intermediate compile results, which can speed up `:advanced` builds by 5x or more (with a hot cache). It's also the only build tool that supports bundling of dependencies for the self-hosted compiler.

To get started:

```
git clone https://github.com/mhuebert/shadow-re-frame.git ;
cd shadow-re-frame;
npm install;
npm run watch;
```

Then, open a browser window to http://localhost:8700.

Press `Control-H` to see the re-frame-trace panel.

Now you should see:

![screenshot](https://i.imgur.com/TK2rO24.png)
