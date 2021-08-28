(ns shadow-re-frame.interop.contracts
  (:require [applied-science.js-interop :as j]
            [promesa.core :as p])
  (:refer-clojure :exclude [name symbol]))

(def contract->address
  {:qi-dao-masterchef "0x574Fe4E8120C4Da1741b5Fd45584de7A5b521F0F"

   :weth "0x7ceB23fD6bC0adD59E62ac25578270cFf1b9f619"
   :am-weth "0x28424507fefb6f7f8E9D3860F56504E4e5f5f390"
   :cam-weth "0x0470CD31C8FcC42671465880BA81D631F0B76C1D"
   :cam-weth-vault "0x11A33631a5B5349AF3F165d2B7901A4d67e561ad"
   :weth-zapper "0xf5C678Be432F07261e728a58bFFEAC52bA731BF5"

   :aave "0xD6DF932A45C0f255f85145f286eA0b292B21C90B"
   :am-Aave "0x1d2a0E5EC8E5bBDCA5CB219e649B565d8e5c3360"
   :cam-aave "0xeA4040B21cb68afb94889cB60834b13427CFc4EB"
   :cam-aave-vault "0x578375c3af7d61586c2C3A7BA87d2eEd640EFA40"

   :wmatic "0x0d500B1d8E8eF31E21C99d1Db9A6444d3ADf1270"
   :am-wmatic "0x8dF3aad3a84da6b69A4DA8aeC3eA40d9091B2Ac4"
   :cam-wmatic "0x7068Ea5255cb05931EFa8026Bd04b18F3DeB8b0B"
   :cam-wmatic-vault "0x88d84a85A87ED12B8f098e8953B322fF789fCD1a"})

(defprotocol IERC20
  (name [this])
  (symbol [this])
  (decimals [this])
  (total-supply [this])
  (balance-of [this address])

  (transfer [this recipient amount])
  (allowance [this owner spender])
  (approve [this spender amount])
  (transfer-from [this sender recipient amount]))

(deftype ERC20 [ethers-contract]
  IERC20
  (name [_]
    (j/call ethers-contract :name))
  (symbol [_]
    (j/call ethers-contract :symbol))
  (decimals [_]
    (j/call ethers-contract :decimals))
  (total-supply [_]
    (j/call ethers-contract :totalSupply))
  (balance-of [_ address]
    (j/call ethers-contract :balanceOf address))
  (allowance [_ owner spender]
    (j/call ethers-contract :allowance owner spender))

  (approve [_ spender amount]
    (j/call ethers-contract :approve spender amount))
  (transfer [_ recipient amount]
    (j/call ethers-contract :transfer recipient amount))
  (transfer-from [_ sender recipient amount]
    (j/call ethers-contract :transferFrom sender recipient amount)))


(comment
 (js/console.log 1)
 (p/let [contract (shadow-re-frame.interop.ethers/fetch-contract
                   (:aave contract->address)
                   (:wmatic contract->address))
         weth-erc20 (ERC20. contract)
         weth-name (name weth-erc20)
         weth-sym (symbol weth-erc20)]

   (js/console.log weth-name)
   (js/console.log weth-sym)))
