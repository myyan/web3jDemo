import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author heiqie
 */
public class WalletDemo {
    private static final Logger log = LoggerFactory.getLogger(WalletDemo.class);
    private Web3j web3j;
    private Credentials credentials;

    public static void main(String[] args) throws Exception {
        new WalletDemo().run();
    }


    private void run() throws Exception {
        log.info("hello eth,hello web3j");
        ethClient();//连接以太坊客户端 connect to eth wallet
        createAccount();//创建冷钱包 generate cold wallet
        loadWallet();//加载钱包 load wallet
        getBalanceOfETH();//查询账户余额 query balance
        transferTo();//转账到指定地址 transfer to specific address
    }


    /**
     * 以太坊客户端
     *
     * @throws IOException  error
     */
    private void ethClient() throws IOException {
        //连接方式1：使用 infura 提供的客户端 use proxy
        //连接方式2：使用本地客户端 use local client
        web3j = Web3j.build(new HttpService("https://ropsten.infura.io/Jj5m4mVrrHBYBH5hyDNc"));
        String web3ClientVersion = web3j.web3ClientVersion().send().getWeb3ClientVersion();
        log.info("version=" + web3ClientVersion);
    }


    /**
     * generate wallet
     * @throws Exception error
     */
    private void createAccount() throws Exception {
        //钱包文件保持路径，请替换位自己的某文件夹路径
        final String walletPath = "./wallet";
        String walletFileName = WalletUtils.generateNewWalletFile("123456", new File(walletPath), false);
        log.info("walletName: " + walletFileName);
    }


    /**
     * load wallet 加载已有的钱包
     *
     * @throws Exception error
     */
    private void loadWallet() throws Exception {
        //钱包路径
        String walletFilePath = "./wallet/UTC--2018-07-16T14-23-31.83000000Z--15d9bf2d7d242f7a828b4060bbcf7e345082451b.json";
        String passWord = "123456";
        credentials = WalletUtils.loadCredentials(passWord, walletFilePath);
        String address = credentials.getAddress();
        BigInteger publicKey = credentials.getEcKeyPair().getPublicKey();
        BigInteger privateKey = credentials.getEcKeyPair().getPrivateKey();

        log.info("address=" + address);
        log.info("public key=" + publicKey);
        log.info("private key=" + privateKey);
    }

    /**
     * 转移以太坊
     *
     * @throws Exception error
     */
    private void transferTo() throws Exception {
        if (web3j == null) {
            return;
        }
        if (credentials == null) {
            return;
        }
        //开始发送0.01 =eth到指定地址
        String addressTo = "0x41F1dcbC0794BAD5e94c6881E7c04e4F98908a87";
        TransactionReceipt send = Transfer.sendFunds(web3j, credentials, addressTo, BigDecimal.ONE, Convert.Unit.FINNEY).send();

        log.info("Transaction complete:");
        log.info("trans hash=" + send.getTransactionHash());
        log.info("from :" + send.getFrom());
        log.info("to:" + send.getTo());
        log.info("gas used=" + send.getGasUsed());
        log.info("status: " + send.getStatus());
    }

    /**
     * 查询地址以太坊余额
     *
     * @throws IOException error
     */
    private void getBalanceOfETH() throws IOException {

        if (web3j == null) {
            return;
        }
        //等待查询余额的地址
        String address = "0x2B2E9F0e9E3C08FeBDe9f498a0631135468edd53";
        //第二个参数：区块的参数，建议选最新区块
        EthGetBalance balance = web3j.ethGetBalance(address, DefaultBlockParameter.valueOf("latest")).send();
        //格式转化 wei-ether
        String balanceETH = Convert.fromWei(balance.getBalance().toString(), Convert.Unit.ETHER).toPlainString().concat(" ether");
        log.info(balanceETH);
    }
}
